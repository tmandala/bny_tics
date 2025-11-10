package com.example.instructions.controller;

import com.example.instructions.model.CanonicalTrade;
import com.example.instructions.model.InboundInstruction;
import com.example.instructions.model.PlatformTrade;
import com.example.instructions.service.TradeService;
import com.example.instructions.util.TradeTransformer;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/trades")
public class TradeController {
    private final TradeService tradeService;
    private final ObjectMapper objectMapper;

    public TradeController(TradeService tradeService, ObjectMapper objectMapper) {
        this.tradeService = tradeService;
        this.objectMapper = objectMapper;
    }

    /** Endpoint for single-instruction JSON */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlatformTrade> acceptJson(@Valid @RequestBody InboundInstruction in) {
        log.debug("Received request to accept json instruction: {}", in);
        CanonicalTrade c = TradeTransformer.toCanonical(in);
        PlatformTrade out = tradeService.processAndPublish(c);
        // Return final JSON (no sensitive raw fields returned)
        return ResponseEntity.ok(out);
    }

    /** Endpoint for File upload: .csv or .json array of objects */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<PlatformTrade>> upload(@RequestPart("file") MultipartFile file) throws Exception {
        log.debug("Upload Start...");
        String name = file.getOriginalFilename() == null ? "" : file.getOriginalFilename().toLowerCase();
        byte[] bytes = file.getBytes();
        List<InboundInstruction> inputs = new ArrayList<>();

        if (name.endsWith(".csv")) {
            CsvMapper mapper = new CsvMapper();
            mapper.registerModule(new JavaTimeModule());
            CsvSchema schema = CsvSchema.emptySchema().withHeader();
            MappingIterator<InboundInstruction> it =
                    mapper.readerFor(InboundInstruction.class).with(schema)
                            .readValues(new String(bytes, StandardCharsets.UTF_8));
            while (it.hasNext()) inputs.add(it.next());
        } else if (name.endsWith(".json")) {
            // parse JSON array start with [
            String json = new String(bytes, StandardCharsets.UTF_8);
            if (json.trim().startsWith("[")) {
                inputs = objectMapper.readerForListOf(InboundInstruction.class).readValue(json);
            } else {  // parse JSON single object
                inputs.add(objectMapper.readValue(json, InboundInstruction.class));
            }
        } else {
            return ResponseEntity.badRequest().build();
        }

        List<PlatformTrade> results = new ArrayList<>();
        for (InboundInstruction in : inputs) {
            CanonicalTrade c = TradeTransformer.toCanonical(in);
            results.add(tradeService.processAndPublish(c));
        }
        log.debug("Results: "+results.toString());
        return ResponseEntity.ok(results);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CanonicalTrade> getCanonical(@PathVariable String id) {
        log.debug("Get Start..."+id);
        CanonicalTrade c = tradeService.get(id);
        return c == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(c);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() { return ResponseEntity.ok("OK"); }
}


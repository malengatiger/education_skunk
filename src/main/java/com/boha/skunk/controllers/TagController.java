package com.boha.skunk.controllers;

import com.boha.skunk.data.Tag;
import com.boha.skunk.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/tags")
public class TagController {
    static final String mm = " \uD83C\uDF4E \uD83C\uDF4E \uD83C\uDF4E " +
            "TagController  \uD83C\uDF4E";
    static final Logger logger = Logger.getLogger(TagController.class.getSimpleName());

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

//    @GetMapping
//    public ResponseEntity<List<Tag>> getAllTags() {
//        List<Tag> tags = tagService.getAllTags();
//        return new ResponseEntity<>(tags, HttpStatus.OK);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Tag> getTagById(@PathVariable Long id) {
//        Optional<Tag> tag = tagService.getTagById(id);
//        return tag.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
//                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }
//    @GetMapping("/createBaseTags")
//    public ResponseEntity<List<Tag>> createBaseTags() {
//        List<Tag> tags = tagService.saveBaseTags();
//        return ResponseEntity.ok(tags);
//    }
//    @GetMapping("/createTag")
//    public ResponseEntity<Tag> createTag(@RequestParam Long subjectId,
//                                         @RequestParam String text,
//                                          @RequestParam int tagType) {
//
//
//        Tag tag = tagService.createTag(subjectId,text,tagType);
//        return ResponseEntity.ok(tag);
//    }
//
//    @PostMapping("/saveTag")
//    public ResponseEntity<Tag> saveTag(@RequestBody Tag tag) {
//        Tag savedTag = tagService.saveTag(tag);
//        return new ResponseEntity<>(savedTag, HttpStatus.CREATED);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
//        tagService.deleteTag(id);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
}

package com.boha.skunk.services;

import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class TagService {
    static final String mm = "\uD83D\uDD90\uD83C\uDFFD\uD83D\uDD90\uD83C\uDFFD\uD83D\uDD90\uD83C\uDFFD " +
            "TagService  \uD83C\uDF4E";
    static final Logger logger = Logger.getLogger(TagService.class.getSimpleName());

//    private final TagRepository tagRepository;
//    private final SubjectRepository subjectRepository;
//
//    @Autowired
//    public TagService(TagRepository tagRepository, SubjectRepository subjectRepository) {
//        this.tagRepository = tagRepository;
//        this.subjectRepository = subjectRepository;
//    }
//
//    public List<Tag> getAllTags() {
//        return tagRepository.findAll();
//    }
//
//    public Optional<Tag> getTagById(Long id) {
//        return tagRepository.findById(id);
//    }
//    public Tag createTag(Long subjectId, String text,
//                                         int tagType) {
//        var subject = subjectRepository.findById(subjectId);
//        Tag t = new Tag();
//        t.setTagType(tagType);
//        t.setSubject(subject.orElse(null));
//        t.setText(text);
//        return saveTag(t);
//    }
//    public Tag saveTag(Tag tag) {
//        Tag t;
//        try {
//            t = tagRepository.save(tag);
//            logger.info(mm+"tag added to db: " + t.getSubject().getTitle()
//                    + " - \uD83D\uDD35 tag: " + t.getText()
//                    + " \uD83D\uDD35 tagType: " + t.getTagType());
//        } catch (DataIntegrityViolationException e) {
//            logger.info(mm+" tag already exists:  " +
//                    "\uD83D\uDC7F \uD83D\uDC7F " + tag.getText());
//            throw e;
//        }
//        return t;
//    }
//    public List<Tag> saveTags(List<Tag> tags) {
//        return tagRepository.saveAll(tags);
//    }
//    public List<Tag> saveBaseTags() {
//        List<Tag> tags = new ArrayList<>();
//        List<Subject> subjects = subjectRepository.findAll();
//        tagRepository.deleteAll();
//        logger.info(mm+"Tags deleted from database ");
//
//        for (Subject subject : subjects) {
//            var tag0 = new Tag(subject,"high school", TagType.HIGH_SCHOOL);
//            var tag1 = new Tag(subject,"tutorials, examples, examinations",TagType.HIGH_SCHOOL);
//            var tag2 = new Tag(subject,"concepts, learning",TagType.HIGH_SCHOOL);
//            tags.add(tag1);
//            tags.add(tag2);
//            tags.add(tag0);
//            //
//            var tag0x = new Tag(subject,"college, university", TagType.COLLEGE);
//            var tag1x = new Tag(subject,"tutorials, examples, examinations",TagType.COLLEGE);
//            var tag2x = new Tag(subject,"concepts, learning",TagType.COLLEGE);
//            tags.add(tag1x);
//            tags.add(tag2x);
//            tags.add(tag0x);
//        }
//        List<Tag> tagsBack = new ArrayList<>();
//        for (Tag tag : tags) {
//            try {
//                var result = tagRepository.save(tag);
//                tagsBack.add(result);
//                logger.info(mm+"Tag added to database: \uD83C\uDF4E"
//                        + result.getSubject().getTitle() + " \uD83C\uDF4E "
//                        + result.getText() + " \uD83D\uDD90\uD83C\uDFFD tagType: " + result.getTagType());
//            } catch (DataIntegrityViolationException e) {
//                logger.severe(mm+"Duplicate ignored: " + tag.getText());
//            }
//
//        }
//        logger.info(mm+"Tags added to database: " + tagsBack.size());
//        return tagsBack;
//    }
//
//    public void deleteTag(Long id) {
//        tagRepository.deleteById(id);
//    }
}

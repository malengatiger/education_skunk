package com.boha.skunk.services;

import com.boha.skunk.data.*;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

@Service
public class SgelaFirestoreService {
    static final String mm = "\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35" +
            " SgelaFirestoreService \uD83C\uDF4E";
    static final Logger logger = Logger.getLogger(SgelaFirestoreService.class.getSimpleName());

    private final Firestore firestore;
    private final FirebaseService firebaseService;
    public SgelaFirestoreService(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
        this.firestore = FirestoreClient.getFirestore();
    }


    public List<String> addUsers(List<User> users) throws ExecutionException, InterruptedException {
        List<String> results = new ArrayList<>();
        for (User user : users) {
            user.setId(user.getId());
            String result = addDocument(user);
            results.add(result);
        }
        return results;
    }

    public List<String> addExamDocuments(List<ExamDocument> users) throws ExecutionException, InterruptedException {
        List<String> results = new ArrayList<>();
        for (ExamDocument examDocument : users) {
            examDocument.setId(examDocument.getId());
            String result = addDocument(examDocument);
            results.add(result);
        }
        return results;
    }

    public List<String> addGeminiResponseRatings(List<GeminiResponseRating> responseRatings) throws ExecutionException, InterruptedException {
        List<String> results = new ArrayList<>();
        for (GeminiResponseRating subscription : responseRatings) {
            subscription.setId(subscription.getId());
            String result = addDocument(subscription);
            results.add(result);
        }
        return results;
    }

    public List<String> addSubscriptions(List<Subscription> subscriptions) throws ExecutionException, InterruptedException {
        List<String> results = new ArrayList<>();
        for (Subscription subscription : subscriptions) {
            subscription.setId(subscription.getId());
            String result = addDocument(subscription);
            results.add(result);
        }
        return results;
    }

    public List<String> addOrganizations(List<Organization> organizations) throws ExecutionException, InterruptedException {
        List<String> results = new ArrayList<>();
        for (Organization organization : organizations) {
            organization.setId(organization.getId());
            String result = addDocument(organization);
            results.add(result);
        }
        return results;
    }

    public List<String> addSubjects(List<Object> subjects) throws ExecutionException, InterruptedException {
        //        for (Subject subject : subjects) {
//            subject.setId(subject.getId());
//            String result = addDocument(subject);
//            results.add(result);
//        }

        return batchWrite(subjects);
    }

    public List<String> addExamLinks(List<ExamLink> examLinks) throws ExecutionException, InterruptedException {
        List<String> results = new ArrayList<>();
        for (ExamLink examLink : examLinks) {
            examLink.setId(examLink.getId());
            String result = addDocument(examLink);
            results.add(result);
        }
        return results;
    }

    public String addDocument(Object data) throws ExecutionException, InterruptedException {
        String name = data.getClass().getSimpleName();
        CollectionReference collectionReference = firestore.collection(name);
        ApiFuture<DocumentReference> result = collectionReference.add(data);
        DocumentReference documentReference = result.get();

        logger.info(mm + "Firestore document added: " + name +
                " \uD83D\uDC99\uD83D\uDC99 path: " + documentReference.getPath());
        return documentReference.getId();
    }

    public Long generateUniqueLong() {
        UUID uuid = UUID.randomUUID();
        long mostSignificantBits = uuid.getMostSignificantBits();
        return Math.abs(mostSignificantBits);
    }

    public void updateDocument(String collectionName, String documentId, Map<String, Object> data) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = firestore.collection(collectionName).document(documentId);
        ApiFuture<WriteResult> result = documentReference.update(data);
        result.get();
    }

    public void deleteDocument(String collectionName, String documentId) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = firestore.collection(collectionName).document(documentId);
        ApiFuture<WriteResult> result = documentReference.delete();
        result.get();
    }

    public <T> T getDocument(String collectionName, String documentId, Class<T> valueType) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = firestore.collection(collectionName).document(documentId);
        ApiFuture<DocumentSnapshot> result = documentReference.get();
        DocumentSnapshot document = result.get();
        if (document.exists()) {
            return document.toObject(valueType);
        }
        return null;
    }

    public  List<String> batchWrite(List<Object> data) throws ExecutionException, InterruptedException {
        String className = data.get(0).getClass().getSimpleName();
        List<String> list = new ArrayList<>();
        WriteBatch batch = firestore.batch();
        CollectionReference collection = firestore.collection(className);

        for (Object item : data) {
            DocumentReference document = collection.document();
            batch.set(document, item);
            list.add(document.getId());
        }

        batch.commit().get();
        logger.info(mm+" Firestore batchWrite complete!. rows added: " + data.size());

        return list;
    }
    public List<String> writeMultipleRows(List<Class<T>> data) {
        logger.info(mm+" Firestore transaction starting. rows to add: " + data.size());
        List<String> documentIds = new ArrayList<>();
        String className = data.get(0).getSimpleName();
        firestore.runTransaction(transaction -> {
            CollectionReference collection = firestore.collection(className);
            for (Object item : data) {
                DocumentReference document = collection.document();
                transaction.set(document, item);
                documentIds.add(document.getId());
            }
            logger.info(mm+" Firestore transaction complete. rows added: " + data.size());
            return null;
        });

        return documentIds;
    }
    public <T> List<T> getAllDocuments(Class<T> valueType) throws ExecutionException, InterruptedException {


        CollectionReference collectionReference = firestore.collection(valueType.getSimpleName());
        ApiFuture<QuerySnapshot> result = collectionReference.get();
        QuerySnapshot querySnapshot = result.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        List<T> objects = new ArrayList<>();
        for (QueryDocumentSnapshot snapshot : documents) {
            objects.add(snapshot.toObject(valueType));
        }
        logger.info(mm + " getAllDocuments: found " + objects.size()
                + "  \uD83D\uDE0E valueType: "
                + valueType.getSimpleName());

        return objects;
    }

    public List<Subject> getSubjects() throws ExecutionException, InterruptedException {
        return getAllDocuments(Subject.class);
    }
    public Subject getSubjectById(Long subjectId) throws Exception {
        var list = getDocumentsByLongProperty(Subject.class.getSimpleName(),
                "id", subjectId);
        if (!list.isEmpty()) {
            return list.get(0).toObject(Subject.class);
        }
        throw new Exception("Subject not found");
    }

    public List<ExamLink> getSubjectExamLinks(Long subjectId) throws ExecutionException, InterruptedException {
        List<QueryDocumentSnapshot> queryDocumentSnapshotList =
                getDocumentsByNestedLongProperty(ExamLink.class.getSimpleName(),
                        "subject.id", subjectId);
        List<ExamLink> examLinks = new ArrayList<>();
        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshotList) {
            ExamLink examLink = snapshot.toObject(ExamLink.class);
            examLinks.add(examLink);
        }

        return examLinks;
    }

    public List<QueryDocumentSnapshot> getDocumentsByNestedLongProperty(String collectionName, String nestedPropertyName, Long propertyValue) throws ExecutionException, InterruptedException {
        CollectionReference collectionReference = firestore.collection(collectionName);
        Query query = collectionReference.whereEqualTo(nestedPropertyName, propertyValue);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        QuerySnapshot snapshot = querySnapshot.get();
        return snapshot.getDocuments();
    }

    public List<GeminiResponseRating> getResponseRatings(Long examLinkId) throws ExecutionException, InterruptedException {
        List<QueryDocumentSnapshot> queryDocumentSnapshotList =
                getDocumentsByLongProperty(GeminiResponseRating.class.getSimpleName(),
                        "examLinkId", examLinkId);
        List<GeminiResponseRating> subjects = new ArrayList<>();
        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshotList) {
            GeminiResponseRating s = snapshot.toObject(GeminiResponseRating.class);
            subjects.add(s);
        }

        return subjects;
    }

    public List<QueryDocumentSnapshot> getDocumentsByLongProperty(String collectionName,
                                                                  String propertyName,
                                                                  Long propertyValue) throws ExecutionException, InterruptedException {
        logger.info(mm + "getDocumentsByLongProperty: propertyName: "
                + propertyName + " propertyValue: " + propertyValue
                + " collectionName: " + collectionName);
        CollectionReference collectionReference = firestore.collection(collectionName);
        Query query = collectionReference.whereEqualTo(propertyName, propertyValue);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        QuerySnapshot snapshot = querySnapshot.get();
        return snapshot.getDocuments();
    }

}

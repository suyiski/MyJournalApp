package com.suizexpressions.myjournalapp.data;

import java.util.HashMap;
import java.util.Map;

public class FirebaseJournalEntry {

        private String id;
        private String title;
        private String content;

        public FirebaseJournalEntry() {
        }

        public FirebaseJournalEntry(String id, String title, String content) {
            this.id = id;
            this.title = title;
            this.content = content;
        }

        public FirebaseJournalEntry(String title, String content) {
            this.title = title;
            this.content = content;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Map<String, Object> toMap() {

            HashMap<String, Object> result = new HashMap<>();
            result.put("title", this.title);
            result.put("content", this.content);

            return result;
        }
}

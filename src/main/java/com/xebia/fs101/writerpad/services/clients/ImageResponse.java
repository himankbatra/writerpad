package com.xebia.fs101.writerpad.services.clients;

public class ImageResponse {

    private String id;
    private ImageUrl urls;

    public static class ImageUrl {
        private String raw;
        private String full;
        private String regular;

        public String getRaw() {
            return raw;
        }

        public void setRaw(String raw) {
            this.raw = raw;
        }

        public String getFull() {
            return full;
        }

        public void setFull(String full) {
            this.full = full;
        }

        public String getRegular() {
            return regular;
        }

        public void setRegular(String regular) {
            this.regular = regular;
        }

        @Override
        public String toString() {
            return regular;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getRegularImageUrl() {
        return urls.getRegular();
    }

    public void setUrls(ImageUrl urls) {
        this.urls = urls;
    }

    @Override
    public String toString() {
        return "ImageResponse{"
                + "id='" + id + '\''
                + ", imageUrl=" + urls
                + '}';
    }
}

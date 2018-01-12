package org.twitbot;

public class ReleaseInformation {

    private String tarball_url;
    private String html_url;
    private String tag_name;
    private String id;

    public String getId() {
        return id;
    }

    public String getHtml_url() {
        return html_url;
    }

    public String getTarball_url() {
        return tarball_url;
    }

    public String getTag_name() {
        return tag_name;
    }

    @Override
    public String toString() {
        return "ReleaseInformation{" +
                "tarball_url='" + tarball_url + '\'' +
                ", html_url='" + html_url + '\'' +
                ", tag_name='" + tag_name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

}
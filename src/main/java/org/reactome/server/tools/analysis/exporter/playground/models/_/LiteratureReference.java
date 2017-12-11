package org.reactome.server.tools.analysis.exporter.playground.models._;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class LiteratureReference {
    private String title;
    private String journal;
    private String pages;
    private long pubMedIdentifier;
    private int volume;
    private int year;
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public long getPubMedIdentifier() {
        return pubMedIdentifier;
    }

    public void setPubMedIdentifier(long pubMedIdentifier) {
        this.pubMedIdentifier = pubMedIdentifier;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "LiteratureReference{" +
                "title='" + title + '\'' +
                ", journal='" + journal + '\'' +
                ", pages='" + pages + '\'' +
                ", pubMedIdentifier=" + pubMedIdentifier +
                ", volume=" + volume +
                ", year=" + year +
                ", url='" + url + '\'' +
                '}';
    }
}

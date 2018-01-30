package pl.prabel.githubdemo.api.model;

import com.appunite.detector.SimpleDetector;
import com.google.common.base.Objects;

import javax.annotation.Nonnull;

/**
 * Created by testday on 30/01/2018.
 */

public class IssueModel implements SimpleDetector.Detectable<IssueModel>{

    private long id;
    private String title;
    private String body;
    private long number;

    public IssueModel(long id, String title, String body, long number) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.number = number;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IssueModel that = (IssueModel) o;

        if (id != that.id) return false;
        if (number != that.number) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        return body != null ? body.equals(that.body) : that.body == null;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, title, body, number);
    }

    @Override
    public boolean matches(@Nonnull IssueModel issueModel) {
        return equals(issueModel);
    }

    @Override
    public boolean same(@Nonnull IssueModel issueModel) {
        return equals(issueModel);
    }
}

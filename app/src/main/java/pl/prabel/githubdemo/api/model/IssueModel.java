package pl.prabel.githubdemo.api.model;

import com.appunite.detector.SimpleDetector;

import javax.annotation.Nonnull;

/**
 * Created by damian on 05.12.17.
 */

public class IssueModel implements SimpleDetector.Detectable<IssueModel> {
    private final int id;
    private final String title;
    private final String body;
    private final int comments;

    public IssueModel(int id, String title, String body, int comments) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.comments = comments;
    }

    public int getComments() {
        return comments;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
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

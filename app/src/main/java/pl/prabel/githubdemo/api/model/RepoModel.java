package pl.prabel.githubdemo.api.model;

import com.appunite.detector.SimpleDetector;
import com.google.common.base.Objects;

import javax.annotation.Nonnull;

public class RepoModel implements SimpleDetector.Detectable<RepoModel> {

    private final String id;
    private final String name;
    private final String fullName;
    private final String description;
    private final int stargazersCount;
    private final RepoOwner owner;
    private final int openIssues;

    public RepoModel(String id, String name, String fullName, String description,
                     int stargazersCount, RepoOwner owner, int openIssues) {
        this.id = id;
        this.name = name;
        this.fullName = fullName;
        this.description = description;
        this.stargazersCount = stargazersCount;
        this.owner = owner;
        this.openIssues = openIssues;
    }

    public RepoOwner getOwner() {
        return owner;
    }

    public int getStargazersCount() {
        return stargazersCount;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RepoModel)) return false;
        RepoModel repoModel = (RepoModel) o;
        return stargazersCount == repoModel.stargazersCount &&
                Objects.equal(id, repoModel.id) &&
                Objects.equal(name, repoModel.name) &&
                Objects.equal(fullName, repoModel.fullName) &&
                Objects.equal(description, repoModel.description);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, fullName, description, stargazersCount);
    }

    @Override
    public boolean matches(@Nonnull RepoModel item) {
        return equals(item);
    }

    @Override
    public boolean same(@Nonnull RepoModel item) {
        return equals(item);
    }

    public int getOpenIssues() {
        return openIssues;
    }
}

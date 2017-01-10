package pl.prabel.githubdemo.api.model;

import com.google.common.base.Objects;

public class RepoOwner {

    private final String login;
    private final String id;

    public RepoOwner(String login, String id) {
        this.login = login;
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RepoOwner)) return false;
        RepoOwner repoOwner = (RepoOwner) o;
        return Objects.equal(login, repoOwner.login) &&
                Objects.equal(id, repoOwner.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(login, id);
    }
}

package pl.prabel.githubdemo.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.base.Objects;

public class User implements Parcelable {
    private final String id;
    private final String login;
    private final String avatarUrl;
    private final String htmlUrl;

    public User(String id, String login, String avatarUrl, String htmlUrl) {
        this.id = id;
        this.login = login;
        this.avatarUrl = avatarUrl;
        this.htmlUrl = htmlUrl;
    }

    public String getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equal(id, user.id) &&
                Objects.equal(login, user.login) &&
                Objects.equal(avatarUrl, user.avatarUrl) &&
                Objects.equal(htmlUrl, user.htmlUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, login, avatarUrl, htmlUrl);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.login);
        dest.writeString(this.avatarUrl);
        dest.writeString(this.htmlUrl);
    }

    protected User(Parcel in) {
        this.id = in.readString();
        this.login = in.readString();
        this.avatarUrl = in.readString();
        this.htmlUrl = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}

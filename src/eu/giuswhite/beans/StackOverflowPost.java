package eu.giuswhite.beans;

import eu.giuswhite.LineCounter;
import eu.giuswhite.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by GiusWhite on 20/01/2016.
 */
public class StackOverflowPost {
    private int id;
    private int postType;
    private int acceptedAnswerId;
    private String tags;
    private String body;

    public StackOverflowPost() {
        this.acceptedAnswerId = -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostType() {
        return postType;
    }

    public void setPostType(int postType) {
        this.postType = postType;
    }

    public int getAcceptedAnswerId() {
        return acceptedAnswerId;
    }

    public void setAcceptedAnswerId(int acceptedAnswerId) {
        this.acceptedAnswerId = acceptedAnswerId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "ID: " + this.getId() + " - PostType: " + this.getPostType() + " - AcceptedAnswerId: " + this.getAcceptedAnswerId();
    }

    public boolean containsCode() {
        return this.getBody().contains(CommonUtils.OPEN_CODE_PATTERN);
    }

    public List<String> getCode() {
        List<String> result = new ArrayList<>();
        Pattern pattern = Pattern.compile(CommonUtils.OPEN_CODE_PATTERN + "(.*?)" + CommonUtils.CLOSED_CODE_PATTERN, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(this.getEscapedBody());
        while (matcher.find()) {
            if (LineCounter.getInstance().getNumberOfLines(matcher.group(1))>4 && matcher.group(1).contains(";")) {
                result.add(matcher.group(1));
            }
        }
        return result;
    }

    public boolean isJavaQuestion() {
        return this.tags.matches("(.*)<java>(.*)");
    }

    private String getEscapedBody() {
        String escaped = this.getBody().replaceAll("&lt;", "<");
        escaped = escaped.replaceAll("&gt;", ">");
        escaped = escaped.replaceAll("&amp;", "&");
        return escaped;
    }
}

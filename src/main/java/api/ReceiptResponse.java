package api;

import com.fasterxml.jackson.annotation.JsonProperty;
import generated.tables.records.ReceiptsRecord;
import generated.tables.records.TagsRecord;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;
import java.util.Base64;
import java.nio.file.Files;
import java.io.File;
import java.io.IOException;

/**
 * This is an API Object.  Its purpose is to model the JSON API that we expose.
 * This class is NOT used for storing in the Database.
 *
 * This ReceiptResponse in particular is the model of a Receipt that we expose to users of our API
 *
 * Any properties that you want exposed when this class is translated to JSON must be
 * annotated with {@link JsonProperty}
 */
public class ReceiptResponse {
    @JsonProperty
    Integer id;

    @JsonProperty
    String merchantName;

    @JsonProperty
    BigDecimal value;

    @JsonProperty
    Timestamp created;

    @JsonProperty
    String [] tags;

    @JsonProperty
    String thumbnail;

    @JsonProperty
    String base64EncodedImage;

    public ReceiptResponse(ReceiptsRecord dbRecord) {
        this.merchantName = dbRecord.getMerchant();
        this.value = dbRecord.getAmount();
        this.created = dbRecord.getUploaded();
        this.id = dbRecord.getId();
        this.thumbnail = dbRecord.getThumbnail();
        this.base64EncodedImage = getBase64EncodedImage(thumbnail);
    }

    public void setTags(List<TagsRecord> tags) {
        ArrayList<String> tagsArray = new ArrayList<>();
        for (TagsRecord tag : tags) {
            tagsArray.add(tag.getTag());
        }
        this.tags = tagsArray.toArray(new String[0]);
    }

    public String getBase64EncodedImage(String filename) {
        File fi = new File("./" + filename + ".png");

        String base64 = "";
        try {
            byte[] fileContent = Files.readAllBytes(fi.toPath());
            base64 = Base64.getEncoder().encodeToString(fileContent);
        } catch(IOException ie) {}
        return base64;
    }
}

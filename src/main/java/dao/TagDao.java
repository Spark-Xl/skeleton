package dao;

import generated.tables.records.TagsRecord;
import generated.tables.records.ReceiptsRecord;

import static generated.Tables.RECEIPTS;
import static generated.Tables.TAGS;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.jooq.Result;

import java.util.List;


public class TagDao {
    DSLContext dsl;

    public TagDao(Configuration jooqConfig) {
        this.dsl = DSL.using(jooqConfig);
    }

    public void updateTag(Integer id, String tagname) {
        List<TagsRecord> tagRecords = dsl.selectFrom(TAGS).where(TAGS.ID.eq(id)).and(TAGS.TAG.eq(tagname)).fetch();

        if (tagRecords.size() == 0) {
            dsl.insertInto(TAGS, TAGS.ID, TAGS.TAG).values(id, tagname).execute();
        } else {
            dsl.deleteFrom(TAGS).where(TAGS.ID.eq(id)).and(TAGS.TAG.eq(tagname)).execute();
        }
    }

    public List<ReceiptsRecord> getReceiptsByTag(String tagname) {
        List<TagsRecord> tagRecords = dsl.selectFrom(TAGS).where(TAGS.TAG.eq(tagname)).fetch();
        return dsl.selectFrom(RECEIPTS).where(RECEIPTS.ID.in(dsl.selectFrom(TAGS).where(TAGS.TAG.eq(tagname)).fetch().getValues(TAGS.ID))).fetch();
    }
}

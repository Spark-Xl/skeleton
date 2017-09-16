package dao;

import api.ReceiptResponse;
import generated.tables.records.ReceiptsRecord;
import generated.tables.records.TagsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static generated.Tables.RECEIPTS;
import static generated.Tables.TAGS;

public class ReceiptDao {
    DSLContext dsl;

    public ReceiptDao(Configuration jooqConfig) {
        this.dsl = DSL.using(jooqConfig);
    }

    public int insert(String merchantName, BigDecimal amount) {
        ReceiptsRecord receiptsRecord = dsl
                .insertInto(RECEIPTS, RECEIPTS.MERCHANT, RECEIPTS.AMOUNT)
                .values(merchantName, amount)
                .returning(RECEIPTS.ID)
                .fetchOne();

        checkState(receiptsRecord != null && receiptsRecord.getId() != null, "Insert failed");

        return receiptsRecord.getId();
    }

    public List<ReceiptResponse> getAllReceipts() {
        List<ReceiptsRecord> records = dsl.selectFrom(RECEIPTS).fetch();
        ArrayList<ReceiptResponse> responses = new ArrayList<>();
        for (ReceiptsRecord record : records) {
            List<TagsRecord> tagRecords = dsl.selectFrom(TAGS).where(TAGS.ID.eq(record.getId())).fetch();
            ReceiptResponse response = new ReceiptResponse(record);
            response.setTags(tagRecords);
            responses.add(response);
        }
        return responses;
    }
}

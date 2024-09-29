package com.dasad.empresa.repository;

import com.dasad.empresa.jooq.tables.records.PasswordResetTokenRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PasswordResetTokenRepository {

    @Autowired
    private DSLContext dsl;

    public void save(PasswordResetTokenRecord tokenRecord) {
        dsl.insertInto(com.dasad.empresa.jooq.tables.PasswordResetToken.PASSWORD_RESET_TOKEN)
                .set(tokenRecord)
                .execute();
    }
}
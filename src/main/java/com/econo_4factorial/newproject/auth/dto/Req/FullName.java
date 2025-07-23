package com.econo_4factorial.newproject.auth.dto.Req;

public record FullName(
        String familyName,

        String givenName
) {
    public String getName() {
        if (this.familyName == null || this.givenName == null ||
                this.familyName.isBlank() || this.givenName.isBlank())
            return null;
        return this.familyName + this.givenName;
    }
}

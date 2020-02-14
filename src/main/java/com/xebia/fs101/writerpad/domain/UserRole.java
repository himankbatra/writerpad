package com.xebia.fs101.writerpad.domain;

public enum UserRole {
    WRITER, EDITOR, ADMIN;

    public String getRoleName() {
        return "ROLE_" + this.name();
    }

}

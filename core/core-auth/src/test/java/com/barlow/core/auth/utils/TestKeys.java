package com.barlow.core.auth.utils;

public enum TestKeys {

    PRIVATE("""
            -----BEGIN PRIVATE KEY-----
            MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAInyqbBHBO3qJywS
            sVtKzv3yDZJLdsJwWzrBZE6+vYDJUia1598iJunWYNj8NKyhKn0Lg/MsOoVRIMae
            WM6vCn1zI5nnZMMKn4oBbejtLnOtgyNQ7LA2LAUwg8s2ia44+EJ1hC6Xk3hh/tgQ
            BlYp3VkJlEeT5tudJeHTk+bjLUV/AgMBAAECgYA9Zbzy0Vk3Tx1qN1Oq70vbqQ0z
            TNUpy3o/V23+wlRz6qbexd3S6U9qilkGmpITN5RCnYp4A/pz9dzhqf6a1zuSWkKU
            8N9n+TZbvWUCs0+K5j+zkc7n/Kocp0ArUkxQMpDwhBkVUsnSEGscAWDZB7z3DStk
            Hjy4G8rCQDtLp5694QJBAOGoLyN7syysX6pplqlzSOG/7C1LaKaS3lTmbfxSbLGN
            dgtXZOx2QfP12wIubMcwm3oMKSXYuqiugtZsViIUWw8CQQCcf0Jl/9sSPupPJYfT
            ChwCbP0LHztmiouFPRBk85gTE1KvaDNYvva19N6B/frzY7jqsE/8Ja6Bc8N4brMS
            Jy6RAkBWTcWhk4jmeBKqkXGe40mnlYiVljazJo7D99Fu5HNPwOO52LXvvhbhYFFf
            1zOhRxTfq9D8+ZQCOaQusAaNSC2/AkAkIRUZKzpOOgwp/YYf6KOSw8qfeLRj9fRD
            7FcOl1YygTGDoVDJWjzmwQNli1cWPZ2BQPcWRTTGWg10jkn1FOqhAkAWdtq6/LGr
            W21pF4R4cgS7vF7bf3ZLGqbgljAZLGXX77ncSdbF+U3zSk0Kf407bTFEuis8xPLh
            FqbpmG/RhdMe
            -----END PRIVATE KEY-----"""),
    PUBLIC("""
            -----BEGIN PUBLIC KEY-----
            MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCJ8qmwRwTt6icsErFbSs798g2S
            S3bCcFs6wWROvr2AyVImteffIibp1mDY/DSsoSp9C4PzLDqFUSDGnljOrwp9cyOZ
            52TDCp+KAW3o7S5zrYMjUOywNiwFMIPLNomuOPhCdYQul5N4Yf7YEAZWKd1ZCZRH
            k+bbnSXh05Pm4y1FfwIDAQAB
            -----END PUBLIC KEY-----""");

    private final String keyString;

    TestKeys(String keyString) {
        this.keyString = keyString;
    }

    public String getKeyString() {
        return keyString;
    }
}

package ru.mtuci.antivirus.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignatureKeys {

    private String privateKey;
    private String publicKey;

    public SignatureKeys() {
        /// Get keys from host environmental variables
        // try {
        //     this.privateKey = System.getenv("PRIVATE_KEY");
        //     this.publicKey = System.getenv("PUBLIC_KEY");
        // } catch (Exception e) {
        //   System.err.println("Failed to get keys from environmental variables: " + e.getMessage());
        // }

        // Const key examples (NOT SECURE TO STORE HERE)
        this.privateKey = "SL2P40G8FN2KWO0E03K23NFNDIWO3O95JTNU38";
        this.publicKey  = "SDLSMZKJCGMTNEOWPQWLQ1P4-0GONIGVMQH3OX";
    }
}

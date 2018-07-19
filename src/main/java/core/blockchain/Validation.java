package core.blockchain;

import chainUtil.ChainUtil;

import java.security.PublicKey;

public class Validation {
    private Validator validator;
    private byte[] signature;


    public Validation(Validator validator, byte[] signature) {
        this.validator = validator;
        this.signature = signature;
    }


    public Validator getValidator() {
        return validator;
    }

    public byte[] getSignature() {
        return signature;
    }


    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    @Override
    public String toString(){

        return "'Validator:'" + this.validator.toString() + "'Signature:'" + ChainUtil.bytesToHex(this.signature);
    }

}

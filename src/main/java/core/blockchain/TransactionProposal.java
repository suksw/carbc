package core.blockchain;

import chainUtil.ChainUtil;
import chainUtil.KeyGenerator;

import java.io.IOException;
import java.security.*;
import java.sql.Timestamp;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class TransactionProposal {
    private PublicKey sender;
    private Validator[] validators;
    private byte[] data;
    private String proposalID;
    private Timestamp timestamp;
    private TransactionInfo transactionInfo;
    private Validation validation;

    //to store send proposals
    private static HashMap<String,TransactionProposal> proposals;


    public TransactionProposal(PublicKey sender, Validator[] validators, byte[] data, String proposalID, Timestamp timestamp, TransactionInfo transactionInfo, Validation validation) {
        this.sender = sender;
        this.validators = validators;
        this.data = data;
        this.proposalID = proposalID;
        this.timestamp = timestamp;
        this.transactionInfo = transactionInfo;
        this.validation = validation;
    }


    public PublicKey getSender() {
        return sender;
    }

    public Validator[] getValidators() {
        return validators;
    }

    public byte[] getData() {
        return data;
    }

    public String getProposalID() {
        return proposalID;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public TransactionInfo getTransactionInfo() {
        return transactionInfo;
    }

    public void setProposalID(String proposalID) {
        this.proposalID = proposalID;
    }

    public Validation getValidation() {
        return validation;
    }

    public void setSender(PublicKey sender) {
        this.sender = sender;
    }

    public void setValidators(Validator[] validators) {
        this.validators = validators;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setTransactionInfo(TransactionInfo transactionInfo) {
        this.transactionInfo = transactionInfo;
    }

    public void setValidation(Validation validation) {
        this.validation = validation;
    }


    //To do


    public static HashMap<String, TransactionProposal> getProposals() {
        return proposals;
    }

    public static void setProposals(HashMap<String, TransactionProposal> proposals) {
        TransactionProposal.proposals = proposals;
    }


    public TransactionProposal createTransactionProposal(){
        //save proposal in proposals hashmap
        return this;
    }

    
    public boolean sendProposal(){
        for (Validator validator: this.validators){
            PublicKey validatorPublicKey = validator.getValidator();
            // create socket connection and send proposal and return true
        }
        return false;
    }

    public TransactionResponse signProposal() throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException, IOException, SignatureException, InvalidKeyException {
            KeyGenerator keygen = new KeyGenerator();
            byte[] signature = ChainUtil.sign(keygen.getPrivateKey(), this.toString());//signature of the proposal
            this.getValidators();

            Validator[] validators = this.getValidators();
            for (Validator validator1:validators){
                if (validator1.getValidator()==keygen.getPublicKey()){
                    Validator  validator = validator1;
                    TransactionResponse response = new TransactionResponse(this.proposalID, validator,signature);
                    keygen = null;
                    return response;
                }
            }
       return null;
    }


    public Block createBlock(String proposalID) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {

        ArrayList<Validation> validations = new ArrayList<Validation>();

        ArrayList responses = TempResponsePool.getResponsePool().get(proposalID);
        TransactionProposal proposal = TransactionProposal.getProposals().get(proposalID);
        String proposalString = proposal.toString();
        for (Object resp:responses){
            TransactionResponse response = (TransactionResponse)resp;

                Validation validation = new Validation(response.getValidator(),response.getSignature());
                validations.add(validation);

        }

        Transaction transaction =new Transaction(this.getSender(),validations,this.data,"",this.getTransactionInfo());

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        byte[] hash = ChainUtil.getHash(transaction.toString());
        BlockHeader blockHeader = new BlockHeader("1",Blockchain.getBlockchainArray().getLast().getHeader().getHash(),hash,timestamp,this.sender,Blockchain.getBlockchainArray().size()+1,true);

        Block block = new Block(blockHeader,transaction);
        return block;
    }



    public void isValid() throws NoSuchAlgorithmException, IOException, SignatureException, NoSuchProviderException, InvalidKeyException, InvalidKeySpecException {
        Scanner scanner = new Scanner(System.in);
        System.out.println(this.toString());
        System.out.println("is this valid? ");
        String isValid = scanner.next();
        if (isValid.equalsIgnoreCase("yes")){
            PublicKey sender = this.getSender();
            TransactionResponse response =  this.signProposal();
            if (response!=null){
                //connection and send
                //sendResponse();
                System.out.println("sending response");
            }
        }
        else if (isValid.equalsIgnoreCase("no")){
            String error = "not agreed with (proposal id)";
            //connection and send
        }else {
            System.out.println("please enter yes or no");
        }
    }
}

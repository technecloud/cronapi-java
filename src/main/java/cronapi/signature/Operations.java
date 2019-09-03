package cronapi.signature;


import cronapi.CronapiMetaData;
import cronapi.ParamMetaData;
import cronapi.Var;
import org.apache.commons.lang3.StringUtils;

import java.io.FileOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@CronapiMetaData(category = CronapiMetaData.CategoryType.OBJECT, categoryTags = {"Assinatura", "Signature"}, helpTemplate = "{{addressOfTheServerBlockDocumentation}}")
public class Operations {

    @CronapiMetaData(type = "function", name = "{{generateKeyPair}}", nameTags = {
            "keypair"}, description = "{{generateKeyPair}}", params = {
            "{{pathOfFile}}", "{{fileName}}", "{{algorithm}}", "{{keySize}}"}, paramsType = {CronapiMetaData.ObjectType.STRING, CronapiMetaData.ObjectType.STRING, CronapiMetaData.ObjectType.STRING, CronapiMetaData.ObjectType.LONG}, returnType = CronapiMetaData.ObjectType.UNKNOWN)
    public static KeyPair generateKeyPair(String path, String fileName,
                                          @ParamMetaData(type = CronapiMetaData.ObjectType.STRING, description = "{{algorithm}}", blockType = "util_dropdown", keys = {
                                                  "DSA", "RSA", "ECDSA", "Ed25519"}, values = {"DSA", "RSA", "ECDSA", "Ed25519"}) String algorithm, int keySize) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        keyPairGenerator.initialize(keySize, new SecureRandom());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        if (StringUtils.isNotEmpty(path) && StringUtils.isNotEmpty(fileName) && StringUtils.isNotBlank(path) && StringUtils.isNotBlank(fileName)) {
            try (FileOutputStream out = new FileOutputStream(path.trim() + fileName + ".key")) {
                out.write(keyPair.getPrivate().getEncoded());
            }

            try (FileOutputStream out = new FileOutputStream(path.trim() + fileName + ".pub")) {
                out.write(keyPair.getPublic().getEncoded());
            }
        }

        return keyPair;
    }

    @CronapiMetaData(type = "function", name = "{{loadPrivateKey}}", nameTags = {
            "privatekey"}, description = "{{loadPrivateKey}}", params = {
            "{{fullFilePath}}", "{{algorithm}}"}, paramsType = {CronapiMetaData.ObjectType.STRING, CronapiMetaData.ObjectType.STRING, CronapiMetaData.ObjectType.STRING, CronapiMetaData.ObjectType.LONG}, returnType = CronapiMetaData.ObjectType.UNKNOWN)
    public static PrivateKey loadPrivateKey(String fullFilePath, @ParamMetaData(type = CronapiMetaData.ObjectType.STRING, description = "{{algorithm}}", blockType = "util_dropdown", keys = {
            "DSA", "RSA", "ECDSA", "Ed25519"}, values = {"DSA", "RSA", "ECDSA", "Ed25519"}) String algorithm) throws Exception {
        byte[] bytes = Files.readAllBytes(Paths.get(fullFilePath));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePrivate(keySpec);
    }

    @CronapiMetaData(type = "function", name = "{{loadPublicKey}}", nameTags = {
            "publickey"}, description = "{{loadPublicKey}}", params = {
            "{{fullFilePath}}", "{{algorithm}}"}, paramsType = {CronapiMetaData.ObjectType.STRING, CronapiMetaData.ObjectType.STRING, CronapiMetaData.ObjectType.STRING, CronapiMetaData.ObjectType.LONG}, returnType = CronapiMetaData.ObjectType.UNKNOWN)
    public static PublicKey loadPublicKey(String fullFilePath, @ParamMetaData(type = CronapiMetaData.ObjectType.STRING, description = "{{algorithm}}", blockType = "util_dropdown", keys = {
            "DSA", "RSA", "ECDSA", "Ed25519"}, values = {"DSA", "RSA", "ECDSA", "Ed25519"}) String algorithm) throws Exception {
        byte[] bytes = Files.readAllBytes(Paths.get(fullFilePath));
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePublic(keySpec);
    }

    @CronapiMetaData(type = "function", name = "{{signObject}}", nameTags = {
            "keypair"}, description = "{{signObject}}", params = {
            "{{content}}", "{{privateKey}}", "{{signatureType}}"}, paramsType = {CronapiMetaData.ObjectType.STRING, CronapiMetaData.ObjectType.STRING, CronapiMetaData.ObjectType.STRING, CronapiMetaData.ObjectType.LONG}, returnType = CronapiMetaData.ObjectType.UNKNOWN)
    public static SignedObject signObject(Var content, PrivateKey privateKey, @ParamMetaData(type = CronapiMetaData.ObjectType.STRING, description = "{{signatureType}}", blockType = "util_dropdown", keys = {
            "SHA1withDSA", "SHA1withRSA", "SHA256withRSA"}, values = {"SHA1withDSA", "SHA1withRSA", "SHA256withRSA"}) String signatureType) throws Exception {
        Signature privateSignature = Signature.getInstance(signatureType);
        return new SignedObject(content.getTypedObject(Serializable.class), privateKey, privateSignature);
    }

    @CronapiMetaData(type = "function", name = "{{verify}}", nameTags = {
            "verify"}, description = "{{verify}}", params = {
            "{{signedObject}}", "{{publicKey}}", "{{signatureType}}"}, paramsType = {CronapiMetaData.ObjectType.STRING, CronapiMetaData.ObjectType.STRING, CronapiMetaData.ObjectType.STRING, CronapiMetaData.ObjectType.LONG}, returnType = CronapiMetaData.ObjectType.UNKNOWN)
    public static boolean verify(SignedObject signedObject, PublicKey publicKey, @ParamMetaData(type = CronapiMetaData.ObjectType.STRING, description = "{{signatureType}}", blockType = "util_dropdown", keys = {
            "SHA1withDSA", "SHA1withRSA", "SHA256withRSA"}, values = {"SHA1withDSA", "SHA1withRSA", "SHA256withRSA"}) String signatureType) throws Exception {
        Signature publicSignature = Signature.getInstance(signatureType);
        return signedObject.verify(publicKey, publicSignature);
    }
}

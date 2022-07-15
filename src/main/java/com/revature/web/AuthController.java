package com.revature.web;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.crypto.Sign.SignatureData;
import org.web3j.utils.Numeric;

import com.revature.dto.UserLoginDTO;
import com.revature.entities.User;
import com.revature.service.UserService;
import com.revature.util.JwtTokenManager;

@RestController
@CrossOrigin(origins="*", allowedHeaders="*")
@RequestMapping("/login")
public class AuthController {
	
	private UserService uServ;
	private JwtTokenManager tokenManager;
	
	@Autowired
	public AuthController(UserService uServ, JwtTokenManager tokenManager) {
		super();
		this.uServ = uServ;
		this.tokenManager = tokenManager;
	}
	
	@PostMapping("/get")
	public UserLoginDTO getNonce(@RequestBody UserLoginDTO loginDetails) {
		User user = uServ.findByPublicAddress(loginDetails.getPublicAddress());
		
		if (user == null) {
			user = new User();
			user.setPublicAddress(loginDetails.getPublicAddress());
			uServ.add(user);
			
			user = uServ.findByPublicAddress(loginDetails.getPublicAddress());
		}
		
		loginDetails.setMessage(String.valueOf(user.getNonce()));

		return loginDetails;
	}

	@PostMapping("/verify")
	public UserLoginDTO signin(@RequestBody UserLoginDTO loginDetails, HttpServletResponse response) {
	    try {
	        // Get the wallet ID and signed message from the body stored in the DTO
	        String publicAddress = loginDetails.getPublicAddress();
	        String message = String.valueOf(loginDetails.getMessage());

	        // Find the nonce from the DB that was used to sign this message
	        User user = uServ.findByPublicAddress(publicAddress);
	        
	        String nonce = user.getNonce();

	        // Generate the HASH of the Nonce
	        byte[] nonceHash = Hash.sha3(("\u0019Ethereum Signed Message:\n" + nonce.length() + nonce).getBytes()); // org.web3j.crypto.Hash

	        // Generate the Signature Data
	        byte[] signatureBytes = Numeric.hexStringToByteArray(message); // org.web3j.utils.Numeric
	        
	        byte v = (byte) ((signatureBytes[64] < 27) ? (signatureBytes[64] + 27) : signatureBytes[63]);
	        byte[] r = Arrays.copyOfRange(signatureBytes, 0, 32);
	        byte[] s = Arrays.copyOfRange(signatureBytes, 32, 64);
	        
	        SignatureData signatureData = new SignatureData(v, r, s); // org.web3j.crypto.Sign.SignatureData

	        // Generate the 4 possible Public Keys
	        List<String> recoveredKeys = new ArrayList<>();
	        for(int i = 0; i < 4; i++) {
	            BigInteger R = new BigInteger(1, signatureData.getR());
	            BigInteger S = new BigInteger(1, signatureData.getS());
	            ECDSASignature ecdsaSignature = new ECDSASignature(R, S);
	            BigInteger recoveredKey = Sign.recoverFromSignature((byte)i, ecdsaSignature, nonceHash);
	        	
	            if(recoveredKey != null) {
	                recoveredKeys.add("0x" + Keys.getAddress(recoveredKey)); // org.web3j.crypto.Keys
	            }
	        }

	        // Check if one of the generated Keys match the public wallet ID.
	        for(String recoveredKey : recoveredKeys) {
	        	System.out.println(recoveredKey);
	        	
	            if(recoveredKey.equalsIgnoreCase(publicAddress)) { 
	            	String token = tokenManager.issueToken(user);
	    			
	    			// append the token to the response in the header
	    			response.addHeader("jwt-token", token);
	    			response.addHeader("Access-Control-Expose-Headers", "jwt-token");
	    			response.setStatus(200);
	    			
	    			user.setNonce(String.valueOf(Long.parseLong(user.getNonce()) * Math.random() + 0.5));
	            	
	    			loginDetails.setMessage(token);
	    			
	                return loginDetails;
	            }
	        }
	        
	        response.setStatus(401);
//	        response.setHeader("TestHeader", "1");
	        return null;
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	response.setStatus(400);
	    	return null;
	    }
	}
	
}

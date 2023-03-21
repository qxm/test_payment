package com.qxm.payment.resources;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qxm.payment.domain.AccountCredentials;
import com.qxm.payment.domain.model.entity.Client;
import com.qxm.payment.domain.service.JwtService;
import com.qxm.payment.domain.service.PaymentService;

/**
 * 
 * @author qxm
 *
 */
@RestController
@RequestMapping("/v1/payment")
public class PaymentController {
	/**
    *
    */
   protected static final Logger logger = Logger.getLogger(PaymentController.class.getName());

	protected PaymentService paymentService;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	/**
	 * 
	 * @param paymentService
	 */
	@Autowired
	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}
	
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> getToken(@RequestBody AccountCredentials credentials) {
		UsernamePasswordAuthenticationToken creds =
				new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword(), Collections.emptyList());
		 logger.info(String.format("payment service login() invoked:%s for %s", credentials.getUsername(), credentials.getPassword()));
		Authentication auth = authenticationManager.authenticate(creds);
		String jwts = jwtService.getToken(auth.getName());
		logger.info(String.format("auth name:%s jwt: %s", auth.getName(), jwts));
		
		return ResponseEntity.ok()
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + jwts)
				.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization")
				.build();
	}
	
	
	/**
	 * 
	 *
	 * @return
	 */
	@RequestMapping(value = "/client", method = RequestMethod.GET)
    public ResponseEntity<?> findByName() {
        logger.info(String.format("payment service findClientByName() invoked:%s", paymentService.getClass().getName()));
        Client entity = null;
        Map<String, Object> map = new HashMap<String, Object>();
        try {
        	String name = getUsername();
        	Optional<Client> result = paymentService.findClientByName(name);
            if (result.isPresent()) {
            	
 
            	entity = result.get();
           
               Client client = (Client)entity;
               map.put("id", client.getId());
               map.put("name", client.getName());
               map.put("balance", client.getBalance().toString());
               logger.info(String.format("payment service findClientByName() get id: %s balance: %s", entity.getId(), client.getBalance().toString()));
               
            }
            
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Exception raised findByClientName REST Call {0}", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return entity != null ? new ResponseEntity<>(map, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
	
	
	private String getUsername() {
		String username = "";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails)principal).getUsername();
		} else {
			username = principal.toString();
		}
		return username;
	}
	/**
	 * 
	 * @param amount
	 * @return
	 */
	@PatchMapping("/topup")
	public ResponseEntity<?> topUp(
	  @RequestBody String amount) {
		logger.info(String.format("payment service topup invoked:%s  amount: %s ", paymentService.getClass().getName(), amount));
		  
	    try {
	    	String username = getUsername();
	    	Optional<Client> client = paymentService.findClientByName(username);
	    	if (client.isPresent()) {
	    		paymentService.topUp(client.get().getId(), amount);
	    	}
			
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Exception raised updateStatus REST Call", ex);
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
	    return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	/**
	 * 
	 * @param amount
	 * @param to
	 * @return
	 */
	@PatchMapping("/pay/{to}")
	public ResponseEntity<?> pay(
	  @RequestBody String amount,  @PathVariable("to") String name) {
		logger.info(String.format("payment service topup invoked:%s to:%s amount: %s ", paymentService.getClass().getName(), name, amount));
		
	    try {
	    	String username = getUsername();
	    	Optional<Client> client = paymentService.findClientByName(username);
	    	Optional<Client> to = paymentService.findClientByName(name);
	    	if (client.isPresent() && to.isPresent()) {
	    		paymentService.pay(client.get().getId(), to.get().getId(), amount);
	    	}
			
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Exception raised updateStatus REST Call", ex);
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
	    return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}

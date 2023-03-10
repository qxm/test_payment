package com.qxm.payment.resources;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qxm.payment.domain.model.entity.BaseEntity;
import com.qxm.payment.domain.model.entity.Client;
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
	
	/**
	 * 
	 * @param paymentService
	 */
	@Autowired
	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/login/{name}", method = RequestMethod.GET)
    public ResponseEntity<?> findById(@PathVariable("name") String name) {
        logger.info(String.format("payment service findClientByName() invoked:%s for %s", paymentService.getClass().getName(), name));
        BaseEntity<?> entity;
        Map<String, Object> map = new HashMap();
        try {
        	entity = paymentService.findClientByName(name);
            if (entity == null) {
            	entity = paymentService.createClient(name);
            }
            if (entity != null) {
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
	
	
	/**
	 * 
	 * @param topUp
	 * @param id
	 * @return
	 */
	@PatchMapping("/{id}/topup")
	public ResponseEntity<?> topUp(
	  @RequestBody String amount, @PathVariable("id") Long id) {
		logger.info(String.format("payment service topup invoked:%s id:%d amount: %s ", paymentService.getClass().getName(), id, amount));
		  
	    try {
			paymentService.topUp(id, amount);
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Exception raised updateStatus REST Call", ex);
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
	    return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	/**
	 * 
	 * @param topUp
	 * @param id
	 * @return
	 */
	@PatchMapping("/{from}/pay/{to}")
	public ResponseEntity<?> pay(
	  @RequestBody String amount, @PathVariable("from") Long from, @PathVariable("to") Long to) {
		logger.info(String.format("payment service topup invoked:%s from:%d ,to:%d amount: %s ", paymentService.getClass().getName(), from, to, amount));
		
	    try {
			paymentService.pay(from, to, amount);
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Exception raised updateStatus REST Call", ex);
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
	    return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}

package com.swp.VinGiG.controller;

import java.io.ObjectInputFilter.Config;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.swp.VinGiG.Config.VNPayConfig;
import com.swp.VinGiG.entity.Deposit;
import com.swp.VinGiG.entity.Provider;
import com.swp.VinGiG.service.DepositService;
import com.swp.VinGiG.service.ProviderService;
import com.swp.VinGiG.utilities.Constants;
import com.swp.VinGiG.view.DepositObject;

@RestController
public class DepositController {
	@Autowired
	private DepositService depositService;
	
	@Autowired
	private ProviderService providerService;
	
	
	@GetMapping("/deposits")
	public ResponseEntity<List<DepositObject>> retrieveAllDeposits(){
		List<Deposit> ls = depositService.findAll();
		List<DepositObject> list = depositService.display(ls);
		return ResponseEntity.ok(list);
    }
	
	@GetMapping("/deposit/{id}")
	public ResponseEntity<DepositObject> retrieveDeposit(@PathVariable int id) {
		Deposit deposit = depositService.findById(id);
		if(deposit != null) {
			List<Deposit> ls = new ArrayList<>();
			ls.add(deposit);
			List<DepositObject> list = depositService.display(ls);
			return ResponseEntity.status(HttpStatus.OK).body(list.get(0));
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/provider/{id}/deposit/date/{dateMinStr}/{dateMaxStr}")
	public ResponseEntity<List<DepositObject>> findByProviderIDInterval(@PathVariable long id, @PathVariable String dateMinStr, @PathVariable String dateMaxStr){
		Date dateMin = Constants.strToDate(dateMinStr);
		Date dateMax = Constants.strToDate(dateMaxStr);
		
		List<Deposit> ls = depositService.findByProviderIDDateInterval(id,dateMin, dateMax);
		List<DepositObject> list = depositService.display(ls);
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
	@GetMapping("/provider/{id}/deposit/pending/date/{dateMinStr}/{dateMaxStr}")
	public ResponseEntity<List<DepositObject>> findByProviderIDIntervalPending(@PathVariable long id, @PathVariable String dateMinStr, @PathVariable String dateMaxStr){
		Date dateMin = Constants.strToDate(dateMinStr);
		Date dateMax = Constants.strToDate(dateMaxStr);
		
		List<Deposit> ls = depositService.findPendingDepositByProviderID(id,dateMin, dateMax);
		List<DepositObject> list = depositService.display(ls);
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
	@GetMapping("/deposit/date/{dateMinStr}/{dateMaxStr}")
	public ResponseEntity<List<DepositObject>> findByDateInterval( @PathVariable String dateMinStr, @PathVariable String dateMaxStr){
		Date dateMin = Constants.strToDate(dateMinStr);
		Date dateMax = Constants.strToDate(dateMaxStr);
		
		List<Deposit> ls = depositService.findByDateInterval(dateMin, dateMax);
		List<DepositObject> list = depositService.display(ls);
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
	@GetMapping("/deposit/method/{method}/date/{dateMinStr}/{dateMaxStr}")
	public ResponseEntity<List<DepositObject>> findByfindByMethod(@PathVariable("method") String method, @PathVariable("dateMin") String dateMinStr, @PathVariable("dateMax") String dateMaxStr){
		Date dateMin = Constants.strToDate(dateMinStr);
		Date dateMax = Constants.strToDate(dateMaxStr);
		
		List<Deposit> ls = depositService.findByMethod(method, dateMin, dateMax);
		List<DepositObject> list = depositService.display(ls);
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
	//CHECKDEPOSIT CHECKSUMs
	@GetMapping("/checkDepositResult/providerID/{providerID}/{string}")
	public  ResponseEntity<String> checksumDeposit(HttpServletRequest request, @PathVariable("providerID") long providerID){
		//Get the latest deposit of provider
		Deposit deposit = depositService.findByProviderIDDateInterval(providerID, null, null).get(0);
		Map fields = new HashMap();
		for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
			String fieldName = (String) params.nextElement();
			String fieldValue = request.getParameter(fieldName);
			if ((fieldValue != null) && (fieldValue.length() > 0)) {
			    fields.put(fieldName, fieldValue);
			}
		}

		String vnp_SecureHash = request.getParameter("vnp_SecureHash");
		if (fields.containsKey("vnp_SecureHashType")) {
			fields.remove("vnp_SecureHashType");
			}
		if (fields.containsKey("vnp_SecureHash")) {
			fields.remove("vnp_SecureHash");
			}
		
		String signValue = VNPayConfig.hashAllFields(fields);
		deposit.setMethod(signValue);
		
		String output = "";
//		if (signValue.equals(vnp_SecureHash)) {
//		    if ("00".equals(request.getParameter("vnp_ResponseCode"))) {
//		        deposit.setSuccess(true);
//		        depositService.confirmDeposit(deposit);
//		        output = "Nạp Tiền Thành Công!";
//		    } else {
//		    	deposit.setSuccess(true);
//		    	output = "Nạp Tiền Thất Bại!";
//		    }
//
//		} else {
//			deposit.setSuccess(false);
//			output = "";
//		}
		
		deposit.setSuccess(true);
        depositService.confirmDeposit(deposit);
        output = "CONGRATULATIONS! You've successfully completed a money deposition! Please come back to your application for new updates!";
		
		return ResponseEntity.ok(output);
	}
	
	@PostMapping("/provider/{id}/deposit")
	public ResponseEntity<Deposit> createDeposit(@PathVariable("id") long id, @RequestBody Deposit deposit, HttpServletRequest request){
		try {
			Provider provider = providerService.findById(id);
			if(provider == null) return ResponseEntity.notFound().header("message", "No Provider with such ID found").build();
			
			if(depositService.findById(deposit.getDepositID()) != null)
				return ResponseEntity.badRequest().header("message", "Deposit with such ID already exists.").build();
			
			deposit.setProvider(provider);
			
			//VNPAY HANDLE
			String vnp_Version = "2.1.0";
	        String vnp_Command = "pay";
	        String orderType = "deposit";
	        long amount = deposit.getAmount()*100;
	        String bankCode = "NCB";
	        
	        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
	        String vnp_IpAddr = VNPayConfig.getIpAddress(request);
	        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
	        
	        Map<String, String> vnp_Params = new HashMap<>();
	        vnp_Params.put("vnp_Version", vnp_Version);
	        vnp_Params.put("vnp_Command", vnp_Command);
	        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
	        vnp_Params.put("vnp_Amount", String.valueOf(amount));
	        vnp_Params.put("vnp_CurrCode", "VND");
	        
	        if (bankCode != null && !bankCode.isEmpty()) {
	            vnp_Params.put("vnp_BankCode", bankCode);
	        }
	        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
	        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
	        vnp_Params.put("vnp_OrderType", orderType);

	        String locate = request.getParameter("language");
	        if (locate != null && !locate.isEmpty()) {
	            vnp_Params.put("vnp_Locale", locate);
	        } else {
	            vnp_Params.put("vnp_Locale", "vn");
	        }
	        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_Returnurl + "/" + provider.getProviderID() + "/resultString");
	        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

	        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
	        String vnp_CreateDate = formatter.format(cld.getTime());
	        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
	        
	        cld.add(Calendar.MINUTE, 15);
	        String vnp_ExpireDate = formatter.format(cld.getTime());
	        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
	        
	        List fieldNames = new ArrayList(vnp_Params.keySet());
	        Collections.sort(fieldNames);
	        StringBuilder hashData = new StringBuilder();
	        StringBuilder query = new StringBuilder();
	        Iterator itr = fieldNames.iterator();
	        while (itr.hasNext()) {
	            String fieldName = (String) itr.next();
	            String fieldValue = (String) vnp_Params.get(fieldName);
	            if ((fieldValue != null) && (fieldValue.length() > 0)) {
	                //Build hash data
	                hashData.append(fieldName);
	                hashData.append('=');
	                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
	                //Build query
	                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
	                query.append('=');
	                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
	                if (itr.hasNext()) {
	                    query.append('&');
	                    hashData.append('&');
	                }
	            }
	        }
	        String queryUrl = query.toString();
	        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
	        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
	        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;
//	        com.google.gson.JsonObject job = new JsonObject();
//	        job.addProperty("code", "00");
//	        job.addProperty("message", "success");
//	        job.addProperty("data", paymentUrl);
//	        Gson gson = new Gson();
//	        resp.getWriter().write(gson.toJson(job));
	        deposit.setMethod(paymentUrl);
	        //END
	        
	        Deposit saveDeposit = depositService.add(deposit);
			return ResponseEntity.status(HttpStatus.CREATED).body(saveDeposit);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "Failed to add new Deposit").build();
		}
	}
	
	@PutMapping("/provider/{id}/deposit")
	public ResponseEntity<Deposit> updateDeposit(@PathVariable("id") long id, @RequestBody Deposit deposit){
		if(depositService.findById(deposit.getDepositID()) == null)
			return ResponseEntity.notFound().header("message", "No Deposit found for such ID").build();
		
		Provider provider = providerService.findById(id);
		if(provider == null) return ResponseEntity.notFound().header("message", "No Provider with such ID found").build();

		deposit.setProvider(provider);
		Deposit updatedDeposit = depositService.update(deposit);
		if(updatedDeposit!= null)
			return ResponseEntity.ok(updatedDeposit);
		else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	@DeleteMapping("/deposit/{id}")
	public ResponseEntity<Void> deleteDeposit(@PathVariable int id){
		try{
			if(depositService.findById(id) == null)
				return ResponseEntity.notFound().header("message", "No Deposit found for such ID").build();
			
			depositService.delete(id);
			return ResponseEntity.noContent().header("message", "deposit deleted successfully").build();
		}
		catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "deposit deletion failed").build();
		}
	}
}

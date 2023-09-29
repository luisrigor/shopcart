package com.gsc.shopcart.utils;

import com.gsc.shopcart.dto.OrderDataDTO;
import com.gsc.shopcart.model.scart.DealerData;
import com.gsc.shopcart.repository.usrlogon.CbusEntityProfileRepository;
import com.gsc.shopcart.repository.usrlogon.LexusEntityProfileRepository;
import com.gsc.shopcart.repository.usrlogon.ToyotaUserEntityProfileRepository;
import com.gsc.as400.al.AlMovement;
import com.gsc.as400.al.AlObservations;
import com.gsc.as400.invoke.InvokeAlInfo;
import com.gsc.shopcart.constants.ApiConstants;
import com.gsc.shopcart.exceptions.ShopCartException;
import com.gsc.shopcart.model.scart.entity.OrderDetail;
import com.gsc.shopcart.model.scart.entity.ProductPriceRule;
import com.gsc.shopcart.security.UserPrincipal;
import com.rg.dealer.Dealer;
import com.rg.objects.DealerCode;
import com.sc.commons.exceptions.SCErrorException;
import com.sc.commons.utils.StringTasks;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
//import org.apache.commons.lang3.StringUtils;
import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Component
@Log4j
public class ShopCartUtils {

    private final ToyotaUserEntityProfileRepository toyotaUserEntityProfileRepository;
    private final CbusEntityProfileRepository cbusEntityProfileRepository;
    private final LexusEntityProfileRepository lexusEntityProfileRepository;

    public ShopCartUtils(ToyotaUserEntityProfileRepository toyotaUserEntityProfileRepository, CbusEntityProfileRepository cbusEntityProfileRepository, LexusEntityProfileRepository lexusEntityProfileRepository) {
        this.toyotaUserEntityProfileRepository = toyotaUserEntityProfileRepository;
        this.cbusEntityProfileRepository = cbusEntityProfileRepository;
        this.lexusEntityProfileRepository = lexusEntityProfileRepository;
    }

    public static boolean isProductInPromotion(LocalDate dtPromoStartLc, LocalDate dtPromoEndLc) {
        Date dtPromoStart = null;
        Date dtPromoEnd = null;

        try {
            dtPromoStart = Date.valueOf(dtPromoStartLc);
        } catch (Exception e) {
            log.error("Error parsing date ", e);
        }

        try {
            dtPromoEnd = Date.valueOf(dtPromoEndLc);
        } catch (Exception e) {
            log.error("Error parsing date ", e);
        }


        boolean isProductInPromotion = false;
        Calendar calNow = Calendar.getInstance();
        if (dtPromoStart != null && dtPromoEnd != null) {
            Calendar calPromoStart = Calendar.getInstance();
            Calendar calPromoEnd = Calendar.getInstance();
            calPromoStart.setTime(dtPromoStart);
            calPromoEnd.setTime(dtPromoEnd);
            // if (calNow.compareTo(calPromoStart)>=0 && calNow.compareTo(calPromoEnd)<=0)
            if (calNow.after(calPromoStart) && calNow.before(calPromoEnd))
                isProductInPromotion = true;
            // if (calNow.compareTo(calPromoEnd)>=0 && ShopCartUtils.isSameDay(calPromoStart, calPromoEnd))
            if (calNow.after(calPromoEnd) && ShopCartUtils.isSameDay(calPromoStart, calPromoEnd))
                isProductInPromotion = false;
            if (ShopCartUtils.isSameDay(calNow, calPromoEnd))
                isProductInPromotion = true;
        } else if (dtPromoStart != null) {
            Calendar calPromoStart = Calendar.getInstance();
            calPromoStart.setTime(dtPromoStart);
            // if (calNow.compareTo(calPromoStart)>=0)
            if (calNow.after(calPromoStart))
                isProductInPromotion = true;
        } else if (dtPromoEnd != null) {
            Calendar calPromoEnd = Calendar.getInstance();
            calPromoEnd.setTime(dtPromoEnd);
            // if (calNow.compareTo(calPromoEnd)<=0)
            if (calNow.before(calPromoEnd))
                isProductInPromotion = true;
        }
        return isProductInPromotion;
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("Os calend�rios n�o podem ser nulos");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    public Hashtable<String, Dealer> getHstDealers(String oidNet) throws SCErrorException {
        if (oidNet.equalsIgnoreCase(Dealer.OID_NET_TOYOTA))
            return getHstDealersToyota();
        else if (oidNet.equalsIgnoreCase(Dealer.OID_NET_LEXUS))
            return getHstDealersLexus();
        else if (oidNet.equalsIgnoreCase(Dealer.OID_NET_CBUS))
            return getHstDealersCbus();
        return new Hashtable<String, Dealer>();
    }

    private static Hashtable<String, Dealer> getHstDealersToyota() throws SCErrorException {
        Hashtable<String, Dealer> HST_DEALERS_TOYOTA = new Hashtable<String, Dealer>();
        HST_DEALERS_TOYOTA = Dealer.getToyotaHelper().getAllActiveMainDealers();
        HST_DEALERS_TOYOTA.put(Dealer.OID_NMSC, Dealer.getHelper().getByObjectId(Dealer.OID_NET_TOYOTA, Dealer.OID_NMSC));
        return HST_DEALERS_TOYOTA;
    }

    private static Hashtable<String, Dealer> getHstDealersLexus() throws SCErrorException {
        Hashtable<String, Dealer> HST_DEALERS_LEXUS = new Hashtable<String, Dealer>();
        HST_DEALERS_LEXUS = Dealer.getLexusHelper().getAllActiveMainDealers();
        HST_DEALERS_LEXUS.put(Dealer.OID_NMSC, Dealer.getHelper().getByObjectId(Dealer.OID_NET_LEXUS, Dealer.OID_NMSC));
        return HST_DEALERS_LEXUS;
    }

    private static Hashtable<String, Dealer> getHstDealersCbus() throws SCErrorException {
        Hashtable<String, Dealer> HST_DEALERS_CBUS = new Hashtable<String, Dealer>();
        HST_DEALERS_CBUS = Dealer.getCBusHelper().getAllActiveMainDealers();
        HST_DEALERS_CBUS.put(Dealer.OID_NMSC, Dealer.getHelper().getByObjectId(Dealer.OID_NET_CBUS, Dealer.OID_NMSC));
        return HST_DEALERS_CBUS;
    }

    public List<Object[]> getSuppliers(Integer idProfileTcap, Integer idProfileSupplier, String oidNet) {
        if (oidNet.equalsIgnoreCase(Dealer.OID_NET_TOYOTA)) {
            return toyotaUserEntityProfileRepository.getSuppliers(idProfileTcap, idProfileSupplier);
        } else if (oidNet.equalsIgnoreCase(Dealer.OID_NET_LEXUS))
            return lexusEntityProfileRepository.getSuppliers(idProfileTcap, idProfileSupplier);
        else
            return cbusEntityProfileRepository.getSuppliers(idProfileTcap, idProfileSupplier);
    }

    public static String getPathCategories(int idCatalog) {
        return "Catalog_" + idCatalog + File.separator + "Categories" + File.separator;
    }

    public static String getPathProductVariants(int idCatalog) {
        return "Catalog_" + idCatalog + File.separator + "Products" + File.separator + "Variants" + File.separator;
    }

    public static String getPathProductItems(int idCatalog) {
        return "Catalog_" + idCatalog + File.separator + "Products" + File.separator + "Items" + File.separator;
    }

    public static double getPriceFor(int totalQuantity, StringBuilder detail, List<ProductPriceRule> vecProductPriceRules) {

        int leftQuantity = totalQuantity;
        double calcCost = 0.00;
        for (ProductPriceRule productPriceRule : vecProductPriceRules) {
            int min = productPriceRule.getMinimumQuantity();
            int mult = productPriceRule.getIncrementalQuantity();
            double prcunit = productPriceRule.getUnitPrice();
            int k;

            if (totalQuantity >= min) { // regra aplic�vel � quantidade remanescente

                k = leftQuantity / mult;
                leftQuantity -= (k * mult);

                double rowCost = (k * mult) * prcunit;

                calcCost += rowCost;
                if ((detail != null) && (k > 0))
                    detail.append("(" + (k * mult) + "*" + prcunit + ")<br>");
            }
        }

        if (leftQuantity != 0)
            calcCost = 0.00;

        return calcCost;
    }

    private Map<String, DealerData> getDealerData(UserPrincipal user, OrderDataDTO orderDataDTO) throws SCErrorException {
        String[] oidDealers = orderDataDTO.getOidDealer().toArray(new String[0]);
        String orderObs = StringTasks.cleanString(orderDataDTO.getOrderObs(), StringUtils.EMPTY);
        Integer multiplicator = (orderDataDTO.getMultiplicator()==null||orderDataDTO.getMultiplicator()<=0)?1:orderDataDTO.getMultiplicator();

        Map<String, DealerData> result = new HashMap<>();

        boolean sendToAS400 = orderDataDTO.getSendToAS400().equals("S");
        for (int i = 0; i < oidDealers.length; i++) {
            // APENAS NECESS�RIO QUANDO ENVIA ORDER PARA AS400
            if (sendToAS400) {
                Dealer oDealerOrder = Dealer.getHelper().getByObjectId(user.getOidNet(), oidDealers[i]);
                Dealer oDealerParentOrder = Dealer.getHelper().getByObjectId(oDealerOrder.getOIdNet(), oDealerOrder.getOid_Parent());

                List<DealerCode> vecDealerCodes1 = oDealerParentOrder.getCodes(Dealer.OID_TOYOTA_CODE_SHIPT_TO_INVOICE);
                List<DealerCode> vecDealerCodes2 = oDealerOrder.getCodes(Dealer.OID_TOYOTA_CODE_SHIPT_TO_INVOICE);
                if (vecDealerCodes1 == null || vecDealerCodes1.isEmpty() || vecDealerCodes2 == null || vecDealerCodes2.isEmpty()) {
                    return null;
                } else {
                    result.put(oidDealers[i], new DealerData(oidDealers[i], orderObs, multiplicator, vecDealerCodes1.get(0).getValue(), vecDealerCodes2.get(0).getValue()));
                }
            } else {
                result.put(oidDealers[i],DealerData.builder().oidDealer(oidDealers[i]).orderObs(orderObs).multiplicator(multiplicator).build());
            }
        }
        return result;
    }
}







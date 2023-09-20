package com.gsc.shopcart.utils;

import com.gsc.as400.al.AlMovement;
import com.gsc.as400.al.AlObservations;
import com.gsc.as400.invoke.InvokeAlInfo;
import com.gsc.shopcart.constants.ApiConstants;
import com.gsc.shopcart.exceptions.ShopCartException;
import com.gsc.shopcart.model.scart.entity.OrderDetail;
import com.rg.dealer.Dealer;
import com.sc.commons.exceptions.SCErrorException;
import com.sc.commons.financial.FinancialTasks;
import com.sc.commons.utils.DateTimerTasks;
import com.sc.commons.utils.ServerTasks;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.*;

@Log4j
public class FileShopUtils {

    public static String setFiles(Map<String, List<OrderDetail>> mapList, Integer idApplication, Integer orderNumber, Dealer dealer) throws SCErrorException {
        int year = DateTimerTasks.getCurYear();
        String registryKeyPrefix = "CRCMP";
        String program = idApplication == ApiConstants.LEXUS_APP ? "CARRCMP-02" : "CARRCMP-01";
        double price;
        int quantity;
        String fileName;
        String fileNameFinal= StringUtils.EMPTY;
        String costCenter = StringUtils.EMPTY;// o mesmo que billTo na base de dados
        String invoiceOperation = "00069";
        String movementType = "D";
        int num=0;
        String group = new DecimalFormat("00").format(year) + new DecimalFormat("0000").format(orderNumber);

        for (Map.Entry<String, List<OrderDetail>> entry : mapList.entrySet()) {
            AlObservations alObs;
            String key = entry.getKey();
            List<OrderDetail> orderDetails = entry.getValue();
            num = 0;

            if (key.length() > 4) costCenter = (key).substring(key.length() - 4);

            List<AlObservations> observationsList = new ArrayList<>();
            List<AlMovement> movementList = new ArrayList<>();

            for (OrderDetail OorderD : orderDetails) {
                String registryKeyAl = registryKeyPrefix + new DecimalFormat("0000").format(year)
                        + new DecimalFormat("0000000").format(OorderD.getIdOrder()) + OorderD.getId();

                AlMovement oAlMovement = new AlMovement();

                oAlMovement.setCostCenter(costCenter);
                oAlMovement.setClientDivisionCode(null);

                oAlMovement.setClientCostCenter(null);
                oAlMovement.setAddressCode("01");

                oAlMovement.setClientNumber(dealer.getToyotaDealerCode());
                oAlMovement.setInvoiceOperation(invoiceOperation);

                oAlMovement.setMovementType(movementType);
                oAlMovement.setCurrencyCode(null);

                price = OorderD.getUnitPrice() / 2;
                quantity = OorderD.getOrderQuantity();

                oAlMovement.setQuantity(quantity);
                oAlMovement.setPrice(price);

                oAlMovement.setVatRate(FinancialTasks.getVAT("PT"));
                oAlMovement.setInvoiceItem("N");
                oAlMovement.setVatSchemeCode("");
                oAlMovement.setVatFree("N");
                oAlMovement.setProgram(program);

                oAlMovement.setRegistryKey(registryKeyAl);

                oAlMovement.setGroup(group);

                oAlMovement.setSapOrder(null);
                oAlMovement.setInvoiceOrderItem(0);

                movementList.add(oAlMovement);

                alObs = populateAlObservations("L", OorderD.getProductReference() + " - " + OorderD.getProductName(), program, registryKeyAl, group);
                observationsList.add(alObs);
                num++;

                if (num == orderDetails.size()) {
                    alObs = populateAlObservations("L", observationsList.size() + 1, "A fatura corresponde � vossa comparticipa��o (50%) no custo dos materiais (MPVs) fornecidos. ", program, registryKeyAl, group);
                    observationsList.add(alObs);
                }
            }
            fileName = writeFiles(movementList, observationsList, entry.getKey());
            fileNameFinal = fileName + ";" + fileName;
        }
        return fileNameFinal;
    }

    private static AlObservations populateAlObservations(String observationType, int lineNumber, String observationsText, String program, String registryKey, String group) {
        AlObservations oAlObservations = populateAlObservations(observationType, observationsText, program, registryKey, group);
        oAlObservations.setLineNumber(lineNumber);
        return oAlObservations;
    }

    private static AlObservations populateAlObservations(String observationType, String observationsText, String program, String registryKey, String group) {
        AlObservations oAlObservations = new AlObservations();
        oAlObservations.setObservationType(observationType);
        oAlObservations.setObservationsText(observationsText);
        oAlObservations.setProgram(program);
        oAlObservations.setRegistryKey(registryKey);
        oAlObservations.setGroup(group);
        return oAlObservations;
    }

    private static String writeFiles(List<AlMovement> vecMovement, List<AlObservations> vecObservations, String orderNumber) {
        try {
            String fileName = "m" + orderNumber;
            String fileObs = "o" + orderNumber;
            String path = "C:\\Users\\Carlos Barrios\\Documents\\tests";
            log.info(path);
            File flMovement = null;
            File flObservations = null;

            String tmp = path + File.separator + fileName;
            String tmpObs = path + File.separator + fileObs;
            log.info(tmp);
            log.info(tmpObs);
            Vector<AlMovement> alMovements = new Vector<>(vecMovement);
            Vector<AlObservations> alObservations = new Vector<>(vecObservations);

            flMovement = InvokeAlInfo.createMovementFile(alMovements, tmp);
            log.info(flMovement);
            flObservations = InvokeAlInfo.createObservationsFile(alObservations, tmpObs);
            log.info(flObservations);
            InvokeAlInfo.GenerateAl(flMovement, flObservations, ServerTasks.getServerType());
            return flMovement.getName();
        } catch (SCErrorException e) {
            throw new ShopCartException("Error Write Files", e);
        }
    }
}

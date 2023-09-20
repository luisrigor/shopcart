package com.gsc.shopcart.dto;

import com.gsc.shopcart.model.scart.entity.Order;
import com.sc.commons.utils.StringTasks;
import lombok.*;

import java.util.Hashtable;
import java.util.Vector;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListOrderDTO {


    int idOrder = StringTasks.cleanInteger(request.getParameter("idOrder"), 0);
    int idOrderDetailStatus = StringTasks.cleanInteger(request.getParameter("idOrderDetailStatus"), -1);
    String criteria = "1=1";
    String msg = PortletTasks.getMessage(request);
    Vector vecOrderDetail = new Vector();
    Vector vecOrderStatus = new Vector();
    Hashtable suppliers = new Hashtable();
    Order order = null;


    String idProfileTcap = request.getPreferences().getValue(ApplicationConfiguration.CONFIGURATION_ID_PROFILE_TCAP, "0");
    String idProfileDealer = request.getPreferences().getValue(ApplicationConfiguration.CONFIGURATION_ID_PROFILE_DEALER, "0");
    String idProfileSupplier = request.getPreferences().getValue(ApplicationConfiguration.CONFIGURATION_ID_PROFILE_SUPPLIER, "0");

    int idCatalog = Integer.parseInt(request.getPreferences().getValue("idCatalog", "0"));
    int idApplication = StringTasks.cleanInteger(request.getPreferences().getValue("idApplication", "0"),0);

}

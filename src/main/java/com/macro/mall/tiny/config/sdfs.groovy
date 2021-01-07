//import com.alibaba.fastjson.*
//import com.casic.base.util.DateUtils
//import com.casic.pms.service.dto.PmsExportExcelDto
//import org.apache.commons.lang3.StringUtils
//Object getData() {
//    PmsExportExcelDto excelDto = exportExcelDto
//    String uiType = excelDto.getUiType()
//    String fileType = excelDto.getFieldType().toLowerCase()
//    String data = ""
//    switch (uiType) {
//        case "date":
//            data = getDataWithDate(value, fileType)
//            break
//        case "number":
//            data = getDataWithNumber(value, fileType)
//            break
//        case "radio":
//        case "checkboxes":
//        case "select":
//        case "userSelect":
//        case "deptSelect":
//        case "functionMatrix":
//            data = getDataWithList(value, fileType)
//            break
//        case "input":
//        case "textarea":
//        default:
//            data = value
//            break
//    }
//    return data
//}
//
//Object getDataWithDate(Object value, String fileType) {
//    message.put("date", "date")
//    if ("string".equals(fileType)) {
//        value = DateUtils.parseInstant(value)
//    }
//    return DateUtils.toChinaDateFormat(value)
//}
//
//Object getDataWithNumber(Object value, String fileType) {
//    message.put("number", "number")
//    if ("string".equals(fileType)) {
//        return Integer.valueOf(value)
//    }
//}
//
//Object getDataWithList(Object value, String fileType) {
//    message.put("list", "list")
//    if ("string".equals(fileType)) {
//        value = JSONArray.parse(value);
//    }else if ("jsonb".equals(fileType)) {
//        value = JSONArray.parse(JSONObject.toJSONString(value));
//    }
//
//    List<String> empList = new ArrayList<>()
//    for (Map map : value) {
//        if (map != null && !map.isEmpty()) {
//            if (StringUtils.isNotBlank(map.get("label"))) {
//                value = map.get("label")
//                empList.add(value);
//            }
//        }
//    }
//    return String.join(",",empList)
//}
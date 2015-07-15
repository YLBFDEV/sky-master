package com.ntko.app.office.documents;

import com.ntko.app.office.documents.support.OfficeSupportCompatV1;

/**
 * Created by WB
 * on 2015/3/27.
 */
@SuppressWarnings("deprecation")
public final class DocumentsAgent extends MobileOfficeConnector {
    /**
     * @param officeCompatCodeCode 版本代码及产品类别
     */
    public static void setOfficeCompatCode(OfficeSupportCompatV1.OfficeCompatCode officeCompatCodeCode) {
        _OfficeCompatCode = officeCompatCodeCode;
    }

    /**
     * @param params 创建Word参数
     */
    public static void createWord(Params params) {
        params.setDocType(Params.DOC_TYPE_MSO);
        params.setSourceType(Params.SourceType.NEW);
        params.setCreateType(Params.MSO_CREATE_TYPE_WORD);
        params.setFileType(Params.FILE_TYPE_WORD);
        editOffice(params);
    }

    /**
     * @param params 创建电子表格参数
     */
    public static void createExcel(Params params) {
        params.setDocType(Params.DOC_TYPE_MSO);
        params.setSourceType(Params.SourceType.NEW);
        params.setCreateType(Params.MSO_CREATE_TYPE_SHEET);
        params.setFileType(Params.FILE_TYPE_EXCEL);
        editOffice(params);
    }


    /**
     * @param params 创建演示文档参数
     */
    public static void createPresentation(Params params) {
        params.setDocType(Params.DOC_TYPE_MSO);
        params.setSourceType(Params.SourceType.NEW);
        params.setCreateType(Params.MSO_CREATE_TYPE_PPT);
        params.setFileType(Params.FILE_TYPE_PRESENTATION);
        editOffice(params);
    }

    /**
     * 编辑Word文档
     *
     * @param params 参数
     */
    public static void editWord(Params params) {
        Params.FILE_TYPE = Params.FILE_TYPE_WORD;
        params.setDocType(Params.DOC_TYPE_MSO);
        params.setFileType(Params.FILE_TYPE_WORD);
        editOffice(params);
    }

    /**
     * 编辑电子表格
     *
     * @param params 参数
     */
    public static void editExcel(Params params) {
        Params.FILE_TYPE = Params.FILE_TYPE_EXCEL;
        params.setDocType(Params.DOC_TYPE_MSO);
        params.setFileType(Params.FILE_TYPE_EXCEL);
        editOffice(params);
    }

    /**
     * 编辑演示文档
     *
     * @param params 参数
     */
    public static void editPresentation(Params params) {
        Params.FILE_TYPE = Params.FILE_TYPE_PRESENTATION;
        params.setDocType(Params.DOC_TYPE_MSO);
        params.setFileType(Params.FILE_TYPE_PRESENTATION);
        editOffice(params);
    }

    /**
     * 编辑PDF文档
     *
     * @param params 参数
     */
    public static void editPDFDocument(Params params) {
        Params.FILE_TYPE = Params.FILE_TYPE_PDF;
        params.setDocType(Params.DOC_TYPE_PDF);
        params.setFileType(Params.FILE_TYPE_PDF);
        _pdfConnV2.edit(params);
    }

    /**
     * 自定义表单域
     *
     * @param formFields 表单域
     */
    public static void setFormFields(CustomFields formFields) {
        if (context != null) {
            fields.fieldsList.clear();
            if (!formFields.fieldsList.isEmpty()) {
                fields.fieldsList.addAll(formFields.fieldsList);
            }
        }
    }

    /**
     * @param params 编辑文档参数
     */
    static void editOffice(Params params) {
        if (params.getUploadOptions() != null) {
            UploadOptions options = new UploadOptions();
            options.setRetry(false);
            options.setActivation(UploadOptions.Activation.ON_DOCUMENT_SAVED);
            params.setUploadOptions(options);
        }
        _officeOps.edit(params);
    }

}

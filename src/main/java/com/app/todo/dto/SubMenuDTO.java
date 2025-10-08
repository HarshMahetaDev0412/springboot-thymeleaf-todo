package com.app.todo.dto;

import com.app.todo.models.Form;

import java.util.ArrayList;
import java.util.List;

public class SubMenuDTO {
    private Long subMenuCd;
    private String subMenuNm;
    private List<Form> forms = new ArrayList<>();

    public SubMenuDTO() {
    }

    public SubMenuDTO(Long subMenuCd, String subMenuNm) {
        this.subMenuCd = subMenuCd;
        this.subMenuNm = subMenuNm;
    }

    public Long getSubMenuCd() {
        return subMenuCd;
    }

    public void setSubMenuCd(Long subMenuCd) {
        this.subMenuCd = subMenuCd;
    }

    public String getSubMenuNm() {
        return subMenuNm;
    }

    public void setSubMenuNm(String subMenuNm) {
        this.subMenuNm = subMenuNm;
    }

    public List<Form> getForms() {
        return forms;
    }

    public void setForms(List<Form> forms) {
        this.forms = forms;
    }

    public void addForm(Form form) {
        this.forms.add(form);
    }
}

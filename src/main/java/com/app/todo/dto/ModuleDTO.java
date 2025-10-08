package com.app.todo.dto;

import com.app.todo.models.ModuleEntity;

import java.util.ArrayList;
import java.util.List;

public class ModuleDTO {
    private ModuleEntity module;
    private List<SubMenuDTO> subMenus = new ArrayList<>();

    public ModuleDTO() {
    }

    public ModuleDTO(ModuleEntity module) {
        this.module = module;
    }

    public ModuleEntity getModule() {
        return module;
    }

    public void setModule(ModuleEntity module) {
        this.module = module;
    }

    public List<SubMenuDTO> getSubMenus() {
        return subMenus;
    }

    public void setSubMenus(List<SubMenuDTO> subMenus) {
        this.subMenus = subMenus;
    }

    public void addSubMenu(SubMenuDTO subMenu) {
        this.subMenus.add(subMenu);
    }
}

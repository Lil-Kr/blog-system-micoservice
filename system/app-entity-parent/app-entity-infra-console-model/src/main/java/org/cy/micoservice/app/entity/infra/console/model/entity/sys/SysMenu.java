package org.cy.micoservice.app.entity.infra.console.model.entity.sys;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author: Lil-K
 * @Date: 2025/3/18
 * @Description:
 */
@Data
@ToString
public class SysMenu {

	private String key;

	private String title; // mapping -> menuName

	private String path; // mapping -> menuUrl

	private String uniqueSign; // 唯一标记

	private List<SysMenu> children = new ArrayList<>();

	public void addSubMenu(SysMenu menu) {
		if (Objects.nonNull(menu)) {
			children.add(menu);
		}
	}
}

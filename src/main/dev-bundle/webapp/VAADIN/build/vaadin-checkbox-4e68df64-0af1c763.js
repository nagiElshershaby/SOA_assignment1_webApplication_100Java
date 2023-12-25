import{e as a,N as r,I as e,b as c,O as o}from"./indexhtml-ec7cfd28.js";const t={selector:"vaadin-checkbox",displayName:"Checkbox",properties:[{propertyName:"--vaadin-checkbox-size",displayName:"Checkbox size",defaultValue:"var(--lumo-font-size-l)",editorType:a.range,presets:r.lumoFontSize,icon:"square"}]},s={selector:"vaadin-checkbox::part(checkbox)",displayName:"Checkbox box",properties:[e.backgroundColor,e.borderColor,e.borderWidth,e.borderRadius]},l={selector:"vaadin-checkbox[checked]::part(checkbox)",stateAttribute:"checked",displayName:"Checkbox box (when checked)",properties:[e.backgroundColor,e.borderColor,e.borderWidth,e.borderRadius]},i={selector:"vaadin-checkbox::part(checkbox)::after",displayName:"Checkmark",properties:[c.iconColor]},b={selector:"vaadin-checkbox label",displayName:"Label",properties:[o.textColor,o.fontSize,o.fontWeight,o.fontStyle]},h={tagName:"vaadin-checkbox",displayName:"Checkbox",elements:[t,s,l,i,b]};export{s as checkboxElement,l as checkedCheckboxElement,i as checkmarkElement,h as default,t as hostElement,b as labelElement};

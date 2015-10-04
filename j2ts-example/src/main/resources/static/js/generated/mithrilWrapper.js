/// <reference path="typings/mithril/mithril.d.ts" />
function BR() {
    return m('br');
}
function TEXT_FIELD(prop, attr) {
    if (attr === void 0) { attr = {}; }
    attr.value = prop();
    attr.onchange = m.withAttr("value", prop);
    return m('input[type=text]', attr);
}
function BUTTON(value, onclick, attr) {
    if (attr === void 0) { attr = {}; }
    attr.onclick = onclick;
    return m('button', attr, value);
}

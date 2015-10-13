/// <reference path="typings/tsd.d.ts" />
var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
var Result = app.CalculateController.Result;
var Input = app.CalculateController.Input;
function esc(value) {
    return value || value === 0 ? value : '';
}
function value(e) {
    return e.target.value;
}
var MyComponent = (function (_super) {
    __extends(MyComponent, _super);
    function MyComponent() {
        var _this = this;
        _super.call(this);
        this.request = function () {
            if (_this.state.arg1 && _this.state.arg2) {
                var input = new Input();
                input.arg1 = Number(_this.state.arg1);
                input.arg2 = Number(_this.state.arg2);
                $.ajax({
                    method: 'GET',
                    url: '/calc',
                    data: input,
                    dataType: 'json',
                    success: function (data, textStatus, jqXHR) {
                        _this.setState({ result: data });
                    }
                });
            }
        };
        this.state = { arg1: '', arg2: '', result: new Result() };
    }
    MyComponent.prototype.render = function () {
        var _this = this;
        return (React.createElement("div", null, "Arg1 ", React.createElement("input", {"type": "text", "value": this.state.arg1, "onChange": function (e) { return _this.setState({ arg1: value(e) }); }}), React.createElement("br", null), "Arg2 ", React.createElement("input", {"type": "text", "value": this.state.arg2, "onChange": function (e) { return _this.setState({ arg2: value(e) }); }}), React.createElement("br", null), React.createElement("button", {"onClick": this.request}, "Calc"), React.createElement("br", null), "Add: ", esc(this.state.result.add), React.createElement("br", null), "Subtract: ", esc(this.state.result.subtract)));
    };
    return MyComponent;
})(React.Component);
React.render(React.createElement(MyComponent, null), document.getElementById('example'));

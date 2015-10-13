/// <reference path="typings/tsd.d.ts" />

import Result = app.CalculateController.Result;
import Input = app.CalculateController.Input;

function esc(value: number | string) {
    return value || value === 0 ? value : '';
}

function value(e: React.FormEvent) {
    return (e.target as any).value;
}

class MyComponent extends React.Component<{}, {arg1?: string, arg2?: string, result?: Result}> {
    constructor() {
        super();
        this.state = {arg1: '', arg2: '', result: new Result()};
    }
    request = () => {
        if (this.state.arg1 && this.state.arg2) {
            const input = new Input();
            input.arg1 = Number(this.state.arg1);
            input.arg2 = Number(this.state.arg2);
            $.ajax({
                method: 'GET',
                url: '/calc',
                data: input,
                dataType: 'json',
                success: (data: Result, textStatus: string, jqXHR: JQueryXHR) => {
                    this.setState({result: data});
                }
            });
        }
    }
    render() {
        return (
          <div>
             Arg1 <input type="text" value={this.state.arg1} onChange={e=>this.setState({arg1: value(e)})} /><br/>
             Arg2 <input type="text" value={this.state.arg2} onChange={e=>this.setState({arg2: value(e)})} /><br/>
             <button onClick={this.request}>Calc</button><br/>
             Add: {esc(this.state.result.add)}<br/>
             Subtract: {esc(this.state.result.subtract)}
          </div>
        );
    }
}

React.render(<MyComponent />, document.getElementById('example'));





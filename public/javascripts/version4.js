console.log("Running version4.js")

const ce = React.createElement

function StatelessHello(props){
    return ce('div', null, `Hello ${props.toWhat}`);
}

class Hello extends React.Component {
    constructor(props) {
        super(props);
        this.state = { clickCount : 0};
    }
    render() {
        return ce('div', {onClick: (e) => this.clickHandler(e)}, `Hello ${this.props.toWhat} - click count ${this.state.clickCount}`);
    }
    clickHandler() {
        this.setState({clickCount: this.state.clickCount + 1});
    }
}

class SimpleForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {textInput: ""}
    }

    render() {
        return ce('input', {
            type:"text",
            value: this.state.textInput,
            onChange: (e) => this.changeHandler(e)}
        );
    }

    changeHandler(e) {
        this.setState({textInput: event.target.value});
    }
}

ReactDOM.render(
    ce('div', null,
        ce(Hello, {toWhat: 'World'}, null),
        ce(StatelessHello, {toWhat: 'Earth'}, null),
        ce(SimpleForm, null, null)
    ),
    document.getElementById('react-root')
);
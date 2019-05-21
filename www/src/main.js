const vueapp = new Vue({
    el: '#app',
    data: () => ({
        reverse: false,
        sortKey: 'rule',
        contextFilter: '',
        ruleFilter: '',
        levelFilter: '',
        messageFilter: '',
        rule: null,
        context: null,
        limit: null,
        level: null,
        records: []
    }),
    methods: {
        getRecords() {
            let head = new Headers();
            head.append('Content-Type', 'application/json');
            head.append('Accept', 'application/json');
            var params = {
                rule: this.rule ? this.rule : null,
                context: this.context ? this.context : null,
                limit: this.limit ? this.limit : 50,
                level: this.level ? this.level : null
            };
            if (this.limit === "0") {
                params.limit = null;
                console.log("here");
            }
            var query = Object.keys(params)
            .filter(k => params[k] !== null)
            .map(k => encodeURIComponent(k) + '=' + encodeURIComponent(params[k]))
            .join('&');

            fetch("https://localhost:8080/api/records?" + query, {
                headers: head,
                method: "GET"
            })
            .then(response => {
                if (response.ok) {
                    return response.json();
                }
                throw Error(response.statusText);
            })
            .then(object => {
                this.records = object;
                this.records.sort((a, b) => a.rule.localeCompare(b.rule));            
            })
            .catch(err => console.log(err.message));
        },
        lowerCase(text) {
            return text.toLowerCase();
        },
        sortBy(sortKey){
            if(this.sortKey == sortKey) {
                //Switch sorting order
                this.reverse = !this.reverse;
                this.records.reverse();
            } else {
                //Switch sortkey
                this.sortKey = sortKey;
                this.reverse = false;
                this.records = this.records.sort((a, b) => a[this.sortKey].localeCompare(b[this.sortKey]));
            }
        },
        filter(context, rule, level, message){
            return context.search(new RegExp(this.contextFilter, "i")) != -1
                && rule.search(new RegExp(this.ruleFilter, "i")) != -1
                && level.search(new RegExp(this.levelFilter, "i")) != -1
                && message.search(new RegExp(this.messageFilter, "i")) != -1;
        }
    }
});

vueapp.getRecords();

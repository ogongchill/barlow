import http from 'k6/http';
import {sleep, check, group} from 'k6';
import {SharedArray} from 'k6/data'

const [secret] = new SharedArray('secret', () => {
    return [JSON.parse(open('../common-secret.json'))]
});
const BASE_URL = secret.baseUrl;
const SLEEP_DURATION = 0.1;

export const options = {
    scenarios: {
        concurrent_test: {
            executor: 'constant-vus',
            vus: 100,
            duration: '30s',
        },
    },
};

export default function () {

}

function subscribe() {
    const url = `${BASE_URL}/api/v1/legislation-accounts/{legislationType}/subscribe/active`;
    const res = http.post(url);
    check(res, {"status is 200": (res) => res.status === 200});
    sleep(SLEEP_DURATION);
}

function unsubscribe() {
    const url = `${BASE_URL}/api/v1/legislation-accounts/{legislationType}/subscribe/deactive`;
    const res = http.post(url);
    check(res, {"status is 200": (res) => res.status === 200});
    sleep(SLEEP_DURATION);
}
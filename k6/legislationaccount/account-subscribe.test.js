import http from 'k6/http';
import {sleep, check, group} from 'k6';
import {SharedArray} from 'k6/data'

const [secret] = new SharedArray('secret', () => {
    return [JSON.parse(open('../common-secret.json'))]
});
const BASE_URL = secret.baseUrl;
const SLEEP_DURATION = 0.1;
const LEGISLATION_TYPE = "LEGISLATION_AND_JUDICIARY";
const VUS = 100;

export const options = {
    scenarios: {
        concurrent_test: {
            executor: 'constant-vus',
            vus: VUS,
            duration: '10s',
        },
    },
};

export function setup() {
    return signup();
}

async function signup() {
    const params = {headers: {"Content-Type": "application/json", "Accept": "application/json"}};
    const tokens = [];
    for (let i = 0; i < VUS; i++) {
        const body = {
            "deviceOs": "ios",
            "deviceId": `k6-test-device-id-${i}`,
            "deviceToken": `k6-test-device-token-${i}`,
            "nickname": `k6-test-user-nickname-${i}`
        }
        const res = await http.asyncRequest(
            'POST', `${BASE_URL}/api/v1/auth/guest/signup`, JSON.stringify(body), params);
        tokens.push(res.json('data').accessToken);
    }
    return {tokens};
}

export default function (data) {
    const token = data.tokens[__VU - 1];
    const params = getRequestParams(token);
    subscribe(params);
}

function retrieveCommittee(params) {
    const url = `${BASE_URL}/api/v1/legislation-accounts/committees/info`;
    const res = http.get(url, params)
    check(res, {"status is 200": (res) => res.status === 200});
    sleep(SLEEP_DURATION);
}

function subscribe(params) {
    const url = `${BASE_URL}/api/v1/legislation-accounts/${LEGISLATION_TYPE}/subscribe/activate`;
    const res = http.post(url, params);
    check(res, {"status is 200": (res) => res.status === 200});
    sleep(SLEEP_DURATION);
}

function getRequestParams(token) {
    return {
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json',
            Accept: 'application/json',
            'X-Client-OS': secret.clientOs,
            'X-Client-OS-Version': secret.clientOsVersion,
            'X-Device-ID': secret.deviceId,
        }
    };
}
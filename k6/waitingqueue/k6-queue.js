import http from 'k6/http';
import { check, sleep } from 'k6';
import { SharedArray } from 'k6/data';
import { randomItem } from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';
import { BASE_URL } from '../common/test.js';

export const options = {
    scenarios: {
        token_test: {
            executor: 'ramping-vus',
            startVUs: 0,
            stages: [
                {duration: '1s', target: 5},
                {duration: '5s', target: 100},
                {duration: '5s', target: 200},
                {duration: '5s', target: 300},
                {duration: '5s', target: 500},
                {duration: '10s', target: 600},
                {duration: '10s', target: 500},
                {duration: '5s', target: 0}
            ],
        },
    }
}

const users = new SharedArray('users', function () {
    return Array.from({ length: 1000 }, (_, i) => i + 1);
});

export default function () {
    const userId = randomItem(users);
    const response = http.post(`${BASE_URL}/issue/${userId}`);

    check(response, {
        'status is 200': (r) => r.status === 200,
    });

    sleep(1);
}
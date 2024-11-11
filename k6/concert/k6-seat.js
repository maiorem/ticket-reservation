import http from 'k6/http';
import { check, sleep } from 'k6';
import { options, BASE_URL } from '../common/test.js';

export { options };

export default function () {
    const seatsResponse = http.get(`${BASE_URL}/api/concert/seats`, {
        headers: { 'token': `token` },
    });

    check(seatsResponse, {
        'seats status was 200': (r) => r.status === 200,
    });

    sleep(1);
}
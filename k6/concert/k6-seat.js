import http from 'k6/http';
import { check, sleep } from 'k6';
import { SharedArray } from 'k6/data';
import { randomItem } from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';
import { options, BASE_URL } from '../common/test.js';

export { options };

const users = new SharedArray('users', function () {
    return Array.from({ length: 1000 }, (_, i) => i + 1);
});

const concerts = new SharedArray('concerts', function () {
    return Array.from({ length: 1000 }, (_, i) => i + 1);
});

const concertDates = new SharedArray('concertDates', function () {
    return Array.from({ length: 1000 }, (_, i) => i + 1);
});

const seats = new SharedArray('seats', function () {
    return Array.from({ length: 1000 }, (_, i) => i + 1);
});

export default function () {
    const userId = randomItem(users);
    const concertId = randomItem(concerts);
    const concertDateId = randomItem(concertDates)
    const seat_id = randomItem(seats)
    const payload = JSON.stringify({
        user_id: userId,
        concert_id : concertId,
        concert_date_id : concertDateId,
        seat_list:[seat_id]
    });
    const params = {
        headers: {
            'Content-Type': 'application/json'
        },
    };
    const seatsResponse = http.post(`${BASE_URL}/concert/seat/apply`, payload, params);

    check(seatsResponse, {
        'seats status was 200': (r) => r.status === 200,
    });

    sleep(1);
}
import axios from 'axios';

const apiClient = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

export default {
  getFlights() {
    return apiClient.get('/flights');
  },
  getCheckin() {
    return apiClient.get('/checkin');
  },
  getBaggage() {
    return apiClient.get('/baggage');
  },
  getNotifications() {
    return apiClient.get('/notifications');
  }
};

import api from '../axiosConfig';

const BASE_URL = '/comments';

export const getCommentsByBookId = (bookId) => api.get(`${BASE_URL}/by-book/${bookId}`);
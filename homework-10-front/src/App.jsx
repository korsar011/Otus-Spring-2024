import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import HomePage from './components/HomePage';
import AuthorList from './components/AuthorList';
import AuthorDetail from './components/AuthorDetail';
import BookList from './components/BookList';
import BookForm from './components/BookForm';
import BookDetail from './components/BookDetail';
import GenreList from './components/GenreList';
import GenreDetail from './components/GenreDetail';
import CommentList from './components/CommentList';

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<HomePage />} />
                <Route path="/authors" element={<AuthorList />} />
                <Route path="/authors/:id" element={<AuthorDetail />} />
                <Route path="/books" element={<BookList />} />
                <Route path="/books/new" element={<BookForm />} />
                <Route path="/books/:id" element={<BookDetail />} />
                <Route path="/books/:id/edit" element={<BookForm />} />
                <Route path="/genres" element={<GenreList />} />
                <Route path="/genres/:id" element={<GenreDetail />} />
                <Route path="/comments/:bookId" element={<CommentList />} />
            </Routes>
        </Router>
    );
}

export default App;
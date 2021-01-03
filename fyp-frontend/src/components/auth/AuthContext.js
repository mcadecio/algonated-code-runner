import React, {createContext, useContext} from 'react';
import AuthState from './AuthState';

const authContext = createContext({});

export const useAuth = () => useContext(authContext);

function AuthProvider({children}) {
    const auth = AuthState();
    return (
        <authContext.Provider value={auth}>
            {children}
        </authContext.Provider>
    );
}

export default AuthProvider;
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Container, TextField, Button, Typography, Box, Snackbar } from '@mui/material';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import MailOutlineIcon from '@mui/icons-material/MailOutline';
import styled from '@emotion/styled';
import './Login.css'; // Importa tu archivo CSS

const StyledContainer = styled(Container)`
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 70vh;
  background-color: #f5f5f5;
`;

const StyledFormContainer = styled.div`
  text-align: center;
  background-color: #fff;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  max-width: 400px;
  width: 100%;
`;

const StyledIconBox = styled(Box)`
  margin: auto;
  margin-bottom: 1rem;
`;

const Login: React.FC = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [openError, setOpenError] = useState(false);
  const navigate = useNavigate();

  const handleLogin = async () => {
    try {
      const response = await fetch('http://localhost:8081/Login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ numeroCuenta: username, password: password }),
      });

      if (!response.ok) {
        throw new Error('Login failed');
      }

      const data = await response.text();
      console.log('Login successful:', data);

      // Simulando un token de sesión almacenado en localStorage
      localStorage.setItem('token', 'dummyToken');

      // Guardar el usuario activo en localStorage
      localStorage.setItem('activeUser', username);

      // Redirigir al usuario dependiendo del estado de autenticación
      if (localStorage.getItem('token')) {
        // Si está autenticado, redirige a dashboard

        navigate('/dashboard');

        window.location.reload();
      } else {
        // Si no está autenticado, muestra un mensaje de error o realiza otra acción
        setError('Credenciales incorrectas. Por favor, verifica tu número de cuenta y contraseña.');
        setOpenError(true);
      }
    } catch (error) {
      console.error('Error:', error);
      setError('Credenciales incorrectas. Por favor, verifica tu número de cuenta y contraseña.');
      setOpenError(true);
    }
  };

  const handleForgotPassword = () => {
    alert('Se enviará un correo electrónico para recuperar la contraseña.');
  };

  const handleCloseError = () => {
    setOpenError(false);
  };

  return (
    <StyledContainer>
      <StyledFormContainer>
        <StyledIconBox>
          <LockOutlinedIcon fontSize="large" color="primary" />
        </StyledIconBox>
        <Typography variant="h5" gutterBottom>
          Iniciar Sesión
        </Typography>
        <TextField
          label="Número de Cuenta"
          fullWidth
          margin="normal"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          InputProps={{
            startAdornment: (
              <Box mr={1}>
                <MailOutlineIcon color="action" />
              </Box>
            ),
          }}
        />
        <TextField
          label="Contraseña"
          type="password"
          fullWidth
          margin="normal"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          InputProps={{
            startAdornment: (
              <Box mr={1}>
                <LockOutlinedIcon color="action" />
              </Box>
            ),
          }}
        />
        <Button variant="contained" color="primary" onClick={handleLogin} fullWidth sx={{ mb: 2 }}>
          Iniciar Sesión
        </Button>
        <Button color="primary" onClick={handleForgotPassword} fullWidth>
          ¿Olvidaste tu contraseña?
        </Button>
        <Snackbar
          anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
          open={openError}
          autoHideDuration={6000}
          onClose={handleCloseError}
          message={error}
          key={'bottomcenter'}
        />
      </StyledFormContainer>
    </StyledContainer>
  );
};

export default Login;

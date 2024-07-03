import React from 'react';
import { Container, Typography, Box, Card, CardContent, Grid } from '@mui/material';
import AccountBalanceIcon from '@mui/icons-material/AccountBalance';
import PaymentIcon from '@mui/icons-material/Payment';
import TrendingUpIcon from '@mui/icons-material/TrendingUp';

const Accounts: React.FC = () => {
  // Información simulada de las cuentas
  const accounts = [
    { id: 1, name: "Cuenta de Ahorros", balance: 1000, icon: <AccountBalanceIcon fontSize="large" />, color: "#2196f3" },
    { id: 2, name: "Cuenta Corriente", balance: 500, icon: <PaymentIcon fontSize="large" />, color: "#4caf50" },
    { id: 3, name: "Cuenta de Inversiones", balance: 2000, icon: <TrendingUpIcon fontSize="large" />, color: "#f44336" },
  ];

  return (
    <Container>
        <br></br>
      <Typography variant="h4" gutterBottom>
        Cuentas
      </Typography>
      <Box sx={{ flexGrow: 1 }}>
        <Grid container spacing={3}>
          {accounts.map((account) => (
            <Grid item xs={12} sm={6} md={4} key={account.id}>
              <Card sx={{ borderLeft: `6px solid ${account.color}`, height: '100%' }}>
                <CardContent>
                  <Box display="flex" alignItems="center" justifyContent="space-between">
                    <Typography variant="h6" component="div">
                      {account.icon} {account.name}
                    </Typography>
                    <Typography variant="body2">
                      Balance: ${account.balance}
                    </Typography>
                  </Box>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      </Box>
    </Container>
  );
};

export default Accounts;

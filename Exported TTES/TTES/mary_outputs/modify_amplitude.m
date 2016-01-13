function y2 =modify_amplitude(y, fs, len)
ramp=0 : 1/len : 1;
ramp=transpose(ramp);
y2=y(1:len).* ramp(1:len);




